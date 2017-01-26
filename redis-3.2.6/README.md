# redis-3.2.6

When this example was made, the latest version of redis was `redis-3.2.6`

## Redis Cluster Golas

`Redis Cluster is a distributed implementation of Redis with the following goals, in order of importance in the design:`

`* High performance and linear scalability up to 1000 nodes. There are no proxies, asynchronous replication is used, and no merge operations are performed on values. `

`* Acceptable degree of write safety: the system tries (in a best-effort way) to retain all the writes originating from clients connected with the majority of the master nodes. Usually there are small windows where acknowledged writes can be lost. Windows to lose acknowledged writes are larger when clients are in a minority partition.`

`* Availability: Redis Cluster is able to survive partitions where the majority of the master nodes are reachable and there is at least one reachable slave for every master node that is no longer reachable. Moreover using replicas migration, masters no longer replicated by any slave will receive one from a master which is covered by multiple slaves.`

## Important

* For install redis see the next link: https://redis.io/topics/quickstart 
* Before to begin is very important to know that redis needs at least 6 instances to create a cluster, por default, three masters and three slaves.

## Usage

1.- Go to `/redis-3.2.6/utils/create-cluster`.

2.- Type `./create-cluster start` to star six instances of redis in cluster mode, the shells includes in this repository are configured to start ports in 7778.

3.- Type `./create-cluster create` to configure cluster.

4.- For see the cluster status, type `redis-cli -c -p 7778` and then `cluster nodes`

`127.0.0.1:7778> cluster nodes`
`5a52f63b9a26f2f6e19870872efadfdff06f86f2 127.0.0.1:7782 slave 0322770c8fc53364dbb969aa86ea37601b8e2a66 0 1485453113290 5 connected`
`275417dfdffe4bb4c7c272b30cd7fcf8664c606e 127.0.0.1:7778 myself,master - 0 0 1 connected 0-5460`
`50339c3d9929443edd301fe73bfdb985c1123c9d 127.0.0.1:7783 slave 570652701b2c91398e3a32cad6c02ddd24b40918 0 1485453112988 6 connected`
`570652701b2c91398e3a32cad6c02ddd24b40918 127.0.0.1:7780 master - 0 1485453113290 3 connected 10923-16383`
`0322770c8fc53364dbb969aa86ea37601b8e2a66 127.0.0.1:7779 master - 0 1485453113290 2 connected 5461-10922`
`8a4e36078b47e2dfd895965f8438dd31303d0112 127.0.0.1:7781 slave 275417dfdffe4bb4c7c272b30cd7fcf8664c606e 0 1485453113088 4 connected`

5.- Type `./create-cluster stop` to stop cluster and instances.
