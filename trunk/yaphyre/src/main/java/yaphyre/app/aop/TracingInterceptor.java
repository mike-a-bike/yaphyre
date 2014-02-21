/*
 * Copyright 2014 Michael Bieri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Created by IntelliJ IDEA.
 * User: michael
 * Date: 21.02.14
 * Time: 14:04
 */
package yaphyre.app.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TracingInterceptor implements MethodInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(TracingInterceptor.class);

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		LOGGER.debug("executing method: " + methodInvocation.getMethod().getDeclaringClass().getName() + "." + methodInvocation.getMethod().getName());
		return methodInvocation.proceed();
	}
}
