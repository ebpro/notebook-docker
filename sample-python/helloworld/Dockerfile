# syntax=docker/dockerfile:1

# Choose the parent image
FROM python:3.11-slim-bullseye

# An argument sets at build command with --build-arg
# It as a default value
ARG BUILD_DATE=1970-01-01T00:00:00Z

# key-value pair as image metadata
LABEL maintainer="emmanuel.bruno@univ-tln.fr"
# See http://label-schema.org/rc1/ for a list of usefull labels
LABEL org.label-schema.build-date=$BUILD_DATE

# An environment variable
ENV NAME="John Doe"

# Creates and moves to a directory
WORKDIR ${WORKDIR}

# Copy the requirements them in the new image.
# Done before the copy of the src to limit cache invalidations
# when only source code changes.
COPY requirements.txt ./

# Installation of the dependencies 
RUN pip install --requirement requirements.txt

# Copy the sources 
COPY hello.py ./

# Set the default command for the image
CMD python ${WORKDIR}/hello.py