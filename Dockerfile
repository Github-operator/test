FROM gradle:latest
WORKDIR /test
ADD . .
ENTRYPOINT ["gradle"]
CMD ["test"]