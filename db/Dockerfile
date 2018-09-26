FROM postgres:9.5
COPY ./import_neu.sql /docker-entrypoint-initdb.d/

ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD feedbacktool
ENV POSTGRES_DB feedbacktool