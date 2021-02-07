# RMI and UDP

Comparing Java RMI and UDP for relative reliability and ease of use.

A simple client passes a specified number of messages to a server. Each message contains a message sequence number and the total number of messages to be sent. The server keeps track of the messages received and, when there are no more messages, outputs a summary of the number of messages received and also which ones were lost.
