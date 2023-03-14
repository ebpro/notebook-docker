BUILDERS="${JUPYTERBOOK_BUILDERS:-singlehtml}"

if docker network ls|grep -q network-book-${PWD##*/}; then
  echo "docker network network-book-${PWD##*/} already exists"
else
  docker network create network-book-${PWD##*/}
fi

if docker container ls|grep -q docker-book-${PWD##*/}; then
  echo "dind docker-book-${PWD##*/} already exists"
else
  docker run --privileged --name docker-book-${PWD##*/} -d --rm \
	--network network-book-${PWD##*/} --network-alias docker \
	-e DOCKER_TLS_CERTDIR=/certs \
	-v docker-book-${PWD##*/}-certs-ca:/certs/ca \
	-v docker-book-${PWD##*/}-certs-client:/certs/client \
	docker:dind
  sleep 5
fi

cmd="docker run --rm --workdir=\"/home/jovyan/booksrc\"\
    --volume $PWD:/home/jovyan/booksrc \
    --env NB_UID=$UID \
    --env JUPYTER_ENABLE_LAB=yes \
    --user root \
    --network network-book-${PWD##*/} \
    -e DOCKER_TLS_CERTDIR=/certs \
    -e DOCKER_HOST='tcp://docker:2376' \
    -e DOCKER_TLS_VERIFY='1' \
    -v docker-book-${PWD##*/}-certs-client:/certs:ro \
    --privileged=true \
    brunoe/jupyterdocker:develop \
		bash -c \
			\" mkdir -p /home/jovyan/.docker/ && \
			cp /certs/* /home/jovyan/.docker/ && \
			for builder in $BUILDERS; do \
				python -m nb_conda_kernels list;\
				jupyter-book build \
					--path-output /home/jovyan/booksrc/.book/ \
					--config /home/jovyan/booksrc/.book/_config.yml \
					--toc /home/jovyan/booksrc/.book/_toc.yml \
					--builder \\\$builder \
					/home/jovyan/booksrc;				
			done && \
			chown -R $UID /home/jovyan/booksrc/.book/ \""

#echo "Running: \($cmd\)"

eval $cmd

docker container stop docker-book-${PWD##*/}
docker volume rm docker-book-${PWD##*/}-certs-ca docker-book-${PWD##*/}-certs-client 
docker network rm network-book-${PWD##*/}
