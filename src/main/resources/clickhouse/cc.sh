#!/bin/bash

# --multiline,  -m – If specified, allow multiline queries (do not send the query on Enter).
# --multiquery, -n – If specified, allow processing multiple queries separated by commas.
#                    Only works in non-interactive mode.
# --time,       -t – If specified, print the query execution time to 'stderr' in non-interactive mode.
cat "$1" | clickhouse-client -mnt
