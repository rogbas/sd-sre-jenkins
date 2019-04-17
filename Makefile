JENKINS_NAMESPACE?=sd-sre-jenkins
JENKINS_TEMPLATE?=jenkins-persistent

.PHONY: deploy
deploy:
	oc new-app -n $(JENKINS_NAMESPACE) $(JENKINS_TEMPLATE);

.PHONY: deploy-jobs
deploy-jobs:
	oc apply -R -f jobs/ -n $(JENKINS_NAMESPACE)