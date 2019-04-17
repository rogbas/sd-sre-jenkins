def label = "mypod-${UUID.randomUUID().toString()}"
podTemplate(cloud: "openshift", label: label, containers: [
    containerTemplate(name: 'golang', image: 'golang:1.10.0', ttyEnabled: true, command: 'cat'),
    containerTemplate(name: 'python', image: 'python:latest', ttyEnabled: true, command: 'cat')
  ]) {

    node(label) {
        stage('Show python version') {
            container('python') {
                sh 'python --version'

            }
        }

        stage('Build Operator') {
            git url: 'https://github.com/openshift/dedicated-admin-operator.git'
            container('golang') {
                sh """
                mkdir -p /go/src/github.com/openshift
                ln -s `pwd` /go/src/github.com/openshift/dedicated-admin-operator
                cd /go/src/github.com/openshift/dedicated-admin-operator && make gobuild
                """
            }
        }

    }
}