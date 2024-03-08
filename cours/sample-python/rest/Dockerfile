FROM python:3.11-slim-bullseye

WORKDIR /src/app

COPY . ./

RUN pip install -r requirements.txt

ENV FLASK_APP=restserver.py
# CMD [ "python", "-m" , "flask", "run", "--host=0.0.0.0" ]
CMD flask run
