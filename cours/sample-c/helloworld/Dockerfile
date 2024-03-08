FROM gcc:12-bullseye as stage-build
WORKDIR /src/app
COPY . /src/app
RUN gcc -Wall -Os helloworld.c -o helloworld

FROM debian:bullseye-slim
COPY --from=stage-build /src/app/helloworld /helloworld
ENTRYPOINT ["/helloworld"]
