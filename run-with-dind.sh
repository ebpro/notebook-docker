#/bin/bash
if docker network ls|grep -q network-${PWD##*/}; then
  echo "docker network network-${PWD##*/} already exists"
else
  docker network create network-${PWD##*/}
fi

if docker container ls|grep -q docker-${PWD##*/}; then
  echo "dind docker-${PWD##*/} already exists"
else
  docker run --privileged --name docker-${PWD##*/} -d --rm \
	--network network-${PWD##*/} --network-alias docker \
	-e DOCKER_TLS_CERTDIR=/certs \
	-v docker-${PWD##*/}-certs-ca:/certs/ca \
	-v docker-${PWD##*/}-certs-client:/certs/client \
	docker:dind
fi

docker run --rm \
 	--network network-${PWD##*/} \
	-e DOCKER_TLS_CERTDIR=/certs \
	-e DOCKER_HOST='tcp://docker:2376' \
	-e DOCKER_TLS_VERIFY='1' \
	-v docker-${PWD##*/}-certs-client:/certs/client:ro \
	--user root \
        --name JupyterDocker-${PWD##*/} \
        --volume $PWD:/home/jovyan/work/${PWD##*/} \
        -p 8888:8888 \
        --env NB_UID=$UID \
        --env JUPYTER_ENABLE_LAB=yes \
	--privileged=true -it \
        brunoe/jupyterdocker:develop start-notebook.sh --notebook-dir=work/${PWD##*/}
