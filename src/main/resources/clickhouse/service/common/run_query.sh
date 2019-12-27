#!/bin/bash
<#--
 #-- Copyright 2019 Kostyantyn Krakovych
 #--
 #-- Licensed under the Apache License, Version 2.0 (the "License");
 #-- you may not use this file except in compliance with the License.
 #-- You may obtain a copy of the License at
 #--
 #--     http://www.apache.org/licenses/LICENSE-2.0
 #--
 #-- Unless required by applicable law or agreed to in writing, software
 #-- distributed under the License is distributed on an "AS IS" BASIS,
 #-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 #-- See the License for the specific language governing permissions and
 #-- limitations under the License.
 #-->
# ClickHouse client option description:
# --multiline,  -m – If specified, allow multiline queries (do not send the query on Enter).
# --multiquery, -n – If specified, allow processing multiple queries separated by commas.
#                    Only works in non-interactive mode.
# --time,       -t – If specified, print the query execution time to 'stderr' in non-interactive mode.
# --query,      -q – The query to process when using non-interactive mode.
source ./${serviceDirectory}/source.sh

clickhouse-client -q "$1"
exit_handler
