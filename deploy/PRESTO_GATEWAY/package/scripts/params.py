#!/usr/bin/env python
# -*- coding: utf-8 -*-
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

from resource_management.libraries.script.script import Script

# config object that holds the configurations declared in the config xml file

config = Script.get_config()

# add new presto cluster need to change the below params
etc_dir = '/etc/presto'

config_directory = '/etc/presto_gateway'
pid_path = '/var/presto/gateway/pid'
bin_path = '/data/opt/presto_gateway/oms_current/gateway-ha-1.6.4-jar-with-dependencies.jar'
daemon_control_script = 'java -jar ' + bin_path + \
                        ' ' + config_directory + '/gateway-ha-config.yml'
