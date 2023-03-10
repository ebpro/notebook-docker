#/bin/bash
docker run --rm \
	--user root \
        --name JupyterDocker-${PWD##*/} \
        --volume $PWD:/home/jovyan/work/${PWD##*/} \
	--volume /var/run/docker.sock:/var/run/docker.sock \
        --publish 8888:8888 \
        --env NB_UID=$UID \
        --env JUPYTER_ENABLE_LAB=yes \
	--privileged=true \
        brunoe/jupyterdocker:develop start-notebook.sh --notebook-dir=work/${PWD##*/}
