FROM openjdk:8
ENV WORK_JAR = 'vandesr-admin-0.0.1-SNAPSHOT.jar'
# 指定镜像的工作目录
WORKDIR /data/server/website
# 将jar包拷贝到工作目录
COPY ./target/${WORK_JAR} ./
EXPOSE 8080/tcp
#健康检测
HEALTHCHECK --start-period=3s CMD wget -O - -q http://${IP: -localhost}/8080
# docker run时触发CMD指令
CMD java -jar ${WORK_JAR}