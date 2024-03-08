FROM alpine
VOLUME /workdir
WORKDIR /workdir
RUN apk --no-cache add sqlite
ENTRYPOINT ["sqlite3","-box","local.db"]
CMD ["--version"]