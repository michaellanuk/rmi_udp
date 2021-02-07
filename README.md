# RMI and UDP

Comparing Java RMI and UDP for relative reliability and ease of use.

A simple client passes a specified number of messages to a server. Each message contains a message sequence number and the total number of messages to be sent. The server keeps track of the messages received and, when there are no more messages, outputs a summary of the number of messages received and also which ones were lost.

## Usage

Compile with just `make` to compile both RMI and UDP clients/servers.

### To specify RMI:

Compile using `make rmi` and run with `./rmiclient.sh` on client machine and `./rmiserver.sh` on server machine.

### To specify UDP:

Compile using `make udp` and run with `./udpclient.sh` on client machine and `./udpserver.sh` on server machine.

Run client executables with target hostname as first argument and number of messages to be sent as second argument.
