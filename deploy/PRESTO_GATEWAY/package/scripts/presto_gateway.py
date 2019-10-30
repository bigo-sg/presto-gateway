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
from resource_management.core.resources.system import Execute
from resource_management.libraries.functions.check_process_status import check_process_status


import sys
import logging
import subprocess

def execute(cmd_list, pid_path):
    p = subprocess.Popen(cmd_list)
    f = open(pid_path, 'wb')
    f.truncate()
    f.write(str(p.pid))

def execute1(cmd_list):
    subprocess.Popen(cmd_list)

logging.basicConfig(stream=sys.stdout)
_LOGGER = logging.getLogger(__name__)
key_val_template = '{0}={1}\n'


class GateWay(Script):
    def install(self, env):
        from params import *
        Execute('mkdir -p ' + config_directory)
        Execute('mkdir -p ' + pid_path)

    def stop(self, env):
        from params import *
        Execute(('/data/opt/presto_gateway/oms_current/gateway.sh',
                 'stop',
                 pid_path + '/process.pid'
                 ))

    def start(self, env):
        from params import *
        from common import get_jvm_params
        self.configure(env)

        jvm_params = get_jvm_params(config_directory + '/presto_gateway.jvm.config')
        Execute(('/data/opt/presto_gateway/oms_current/gateway.sh',
                 'start',
                 pid_path + '/process.pid',
                 bin_path,
                 jvm_params,
                 config_directory + '/gateway-ha-config.yml',
                 ),
                environment={'JAVA_HOME': '/data/opt/jdk/current'})

    def status(self, env):
        from params import *
        check_process_status(pid_path + '/process.pid')

    def configure(self, env):
        from params import *
        conf = config['configurations']
        gateway_config = conf['gateway-ha-config.yml']
        f = open(config_directory + '/gateway-ha-config.yml', 'wb')
        f.write(gateway_config['gateway-ha-config.yml'])
        f.close()

        gateway_config = conf['presto_gateway.jvm.config']
        f = open(config_directory + '/presto_gateway.jvm.config', 'wb')
        f.write(gateway_config['presto_gateway.jvm.config'])
        f.close()

if __name__ == '__main__':
    GateWay().execute()
