/**
 *   Copyright © 2011 Aftab Mahmood
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 **/
package org.easy.ldap;

import org.apache.commons.logging.Log;

/**
 * @author  mahmood.aftab
 *
 */
public class LoggingUtil
{
    public static void createDebugLog(Log log, String methodName, Object...arguments)
    {        
        if (log.isDebugEnabled())
        {
            StringBuilder sb = new StringBuilder();
            sb.append(methodName).append("(");            
            
            for (Object argument : arguments)
            {
                sb.append(argument).append(", ");
            }
            
            sb.append(methodName).append(")");     
            
            log.debug(sb.toString());
        }
    }
}
