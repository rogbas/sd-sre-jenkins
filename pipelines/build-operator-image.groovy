def label = "mypod-${UUID.randomUUID().toString()}"
def operatorDockerfile ="build/Dockerfile"
def operatorImageURI = "quay.io/rogbas/dedicated-admin-operator:latest"

podTemplate(cloud: "openshift", label: label, containers: [
    containerTemplate(name: 'golang', image: 'golang:1.10.0', ttyEnabled: true, command: 'cat'),
    containerTemplate(name: 'python', image: 'python:latest', ttyEnabled: true, command: 'cat'),
    containerTemplate(name: 'docker', image: 'docker:18.09', command: 'cat', ttyEnabled: true),
  ],
  volumes:[
    hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'),
]) {

    node(label) {
        stage('Show python version') {
            container('python') {
                sh 'python --version'

            }
        }

        stage('Build Operator Image') {
            git url: 'https://github.com/openshift/dedicated-admin-operator.git'
            container('docker') {
                sh "docker build . -f ${operatorDockerfile} -t ${operatorImageURI}"
            }
        }

    }
}