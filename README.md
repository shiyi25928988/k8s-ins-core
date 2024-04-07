# Introduction
This a K8S fast install tool based on jsch, which can install a master node in 3 to 5min, and a worker node in 1 to 3min.
basically, we use it to establish a dev or test environments. 
The following parts will interpret howto use it.


#### 1. use **'run -h'** to get help

2. use 'run -t' to generate a template file
after generating a template file named as 'k8s-cluster-config.json' , and we fill the template file as: 
```json
{
    "NODES":[
        {
            "HOST_IP":"192.168.1.101",
            "HOST_NAME":"master01",
            "JOIN_CMD":"",
            "PASSWORD":"1234567890",
            "PORT":"22",
            "USER":"root"
        }
    ]
}

```

3. Use **'run -i k8s-cluster-config.json -v 1.28.2' to install a master node.
> It currently supports 1.28.2 and 1.21.1 versions of k8s.

4. After installing the master node, we can check the logs file int 'log' folder, which named with the ip address of the master node.
And we can see the 'kubeadm join' command. and put it into the worker node configuration file.
```json
{
    "NODES":[
        {
            "HOST_IP":"192.168.1.102",
            "HOST_NAME":"worker01",
            "JOIN_CMD":"kubeadm join 192.168.3.137:6443 --token muw7tj.6znrnqcxxekbh8a9 --discovery-token-ca-cert-hash sha256:1a48921dc8a665b8ac84efbbf23c72a54af44bd72c02552a92605b5aca303377",
            "PASSWORD":"1234567890",
            "PORT":"22",
            "USER":"root"
        }
    ]
}

```
and use 'run -i k8s-cluster-config.json -v 1.28.2' to install a worker node.

> it need a clean environment. any pre installed 'docker' or 'containerd' may cause the failure of installation.