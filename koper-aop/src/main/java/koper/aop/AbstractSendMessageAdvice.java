/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package koper.aop;

import koper.sender.MessageSender;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * AbstractSendMessageAdvice
 *
 * @author kk hekun@zhai.me
 * @since 1.0
 * 2016年1月20日
 */
public abstract class AbstractSendMessageAdvice implements ApplicationContextAware  {
	
	private static Logger log = LoggerFactory.getLogger(AbstractSendMessageAdvice.class);
	
	protected ApplicationContext applicationContext;
	@Autowired
	protected MessageSender messageSender;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	/**
	 * @param messageSender the messageSender to set
	 */
	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}
	
	/**
	 * @return the messageSender
	 */
	public MessageSender getMessageSender() {
		if(messageSender!=null)
			return messageSender;
		else
			return (MessageSender) applicationContext.getBean("messageSender");
	}
	/**
	 * SendMsg 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		Object result = null;
		
		Method method = getMethod(pjp);
		String beforeTopic = getBeforeSendMsgTopicOnMethod(method);
		Object[] args = pjp.getArgs();
		
		if(beforeTopic != null) {
//			if(log.isDebugEnabled()) 
//				log.debug("BeforeTopic='{}',Method={} "  ,beforeTopic, method);
			sendMessageBefore(method, beforeTopic,args);
		}
		
		String afterTopic = getAfterSendMsgTopicOnMethod(method);
		try {
			//调用实际代码
			result = pjp.proceed(args);
			
			if( afterTopic != null ) {
//				if(log.isDebugEnabled()) 
//					log.debug("AfterTopic='{}', Method {}"  ,afterTopic, method);
				sendMessageAfter(method, afterTopic,args,result);
			}
			
		} catch (Throwable e) {
			
//			if(log.isDebugEnabled()) 
//				log.debug("sendMessageAfterException exception " + e);
			
			try { sendMessageAfterException(method,afterTopic,args,e); } catch(Throwable t) { /* do nothing */ }
			
			throw e;
		}
		if(log.isDebugEnabled()) 
			log.debug("SendMsgBeforeInvokeAdvice result " + result);
		return result;
	}
	
	/**
	 *  Send message after method invocation,using args and return values
	 * @param targetClass
	 * @param method
	 * @param args
	 */
	protected void sendMessageBefore(Method method,String topic, Object[] args) {
//		if(log.isDebugEnabled()) 
//			log.debug("[发送调用前消息]Send message before invoke, Topic={},Method={}, Args:{}",topic,method,Arrays.asList(args));
	}
	
	/**
	 * Send message before method invocation, using args
	 * @param targetClass
	 * @param method
	 * @param args
	 */
	protected void sendMessageAfter(Method method,String topic, Object[] args,Object result) {
//		if(log.isDebugEnabled()) 
//			log.debug("[发送调用后消息]Send message after invoke,Topic={}, Method={}, Args:{},Result:{}",topic, method,Arrays.asList(args),result);
	}
	
	/**
	 * 
	 * @param targetClass
	 * @param method
	 * @param args
	 */
	protected void sendMessageAfterException(Method method,String topic, Object[] args,Throwable t) {
//		if(log.isDebugEnabled()) 
//			log.debug("[发送调用异常后消息]Send message after invoke,Topic={},  Method={}, Args:{},Throwable:{}",topic,method,Arrays.asList(args),t);
	}
	
	
	

	
	/**
	 * Get before Topic Name on Method
	 * @param pjp
	 * @return
	 * @throws NoSuchMethodException
	 */
	protected String getBeforeSendMsgTopicOnMethod(Method method) throws NoSuchMethodException {
		SendMessageBefore sendMsgannotation = method.getAnnotation( SendMessageBefore.class);
		String topic = sendMsgannotation!=null ? sendMsgannotation.topic() : null;

		if( topic == null) {
			 topic = buildTopicName(method);
			 topic = fixTopicName( topic );
		}
		
		return topic;
	}

	
	/**
	 * Get after Topic Name on Method
	 * @param pjp
	 * @return
	 * @throws NoSuchMethodException
	 */
	protected String getAfterSendMsgTopicOnMethod(Method method) throws NoSuchMethodException {
		SendMessageAfter sendMsgannotation = method.getAnnotation( SendMessageAfter.class );
		String topic = sendMsgannotation!=null ? sendMsgannotation.topic() : null;
		
		if( topic==null) {
			 topic = buildTopicName( method );
			 topic = fixTopicName( topic );
		}
		
		return topic;
	}
	
	/**
	 * 构造消息topic name
	 * @param method
	 */
	protected String buildTopicName(Method method) {
		 String topic = method.getDeclaringClass().getName() +"." + method.getName();
		 return topic;
	}

	private final static Map<String,Method> methodCache = new ConcurrentHashMap<>(1024);
	/**
	 * Get method of joinpoint
	 * @param pjp
	 * @return
	 */
	protected Method getMethod(ProceedingJoinPoint pjp) {
		
		Class<?> clazz = pjp.getTarget().getClass();
		String methodName = pjp.getSignature().getName();
		Method  method = null;
		
		int argCount =  pjp.getArgs().length;
//		Object[] args = pjp.getArgs();
//		Class<?>[] argClasses = new Class[args.length];
//		for (int i = 0; i < args.length; i++) {
//			Class<?> argClass = args[i].getClass();
//			
//			argClass = convertActualClass( argClass );
//			argClasses[i] = args[i]!=null ? args[i].getClass() : null;
//		}
	    try {
	    	String key = clazz.getName() + "." + methodName +"#" + argCount;
	    	if( (method = methodCache.get(key)) == null ) {
	    		method = getMethodFromClass(clazz, methodName, argCount);
	    		
	    		if(method!=null) {
	    			methodCache.put(key, method);
	    		}else {
	    			log.error("AOP pointcut method not found! methodName={},argCount={}",methodName,argCount);
	    		}
	    	}
    		
		} catch (Exception e) {
			throw new RuntimeException("Method not found!" + clazz.getName() +"." + methodName, e);
		}
		return method;
	}
	
	/**
	 * 查找方法。按methodName 和 参数个数进行匹配。
	 * @param clazz
	 * @param methodName
	 * @param argCount
	 * @return
	 * @throws NoSuchMethodException
	 */
	protected Method getMethodFromClass(Class<?> clazz, String methodName, int argCount) throws NoSuchMethodException {
		
		Method[] methods = clazz.getMethods();
		for (Method method0 : methods) {
			String methodName0 =  method0.getName();
			if( methodName0.equals(methodName) && method0.getParameterCount() == argCount ) {
				return method0;
			}
		}
		return null;
	}
	
//	private static Map<String,Class<?>> primitiveClassMap = new HashMap<String,Class<?>>();
	
//	static {
//		primitiveClassMap.put(Integer.class.getName(), int.class);
//		primitiveClassMap.put(Long.class.getName(), long.class);
//		primitiveClassMap.put(Boolean.class.getName(), boolean.class);
//		primitiveClassMap.put(Double.class.getName(), double.class);
//		primitiveClassMap.put(Float.class.getName(), float.class);
//		primitiveClassMap.put(Byte.class.getName(), byte.class);
//		primitiveClassMap.put(Short.class.getName(), short.class);
//	}
//	
//	/**
//	 * @param argClass
//	 * @return
//	 */
//	private Class<?> convertActualClass(Class<?> argClass) {
//		if(argClass.isPrimitive()) {
//			return  primitiveClassMap.get(argClass.getName());
//		}else
//			return argClass;
//	}

	protected String fixTopicName( String topicName0) {
		String topicNameStr = topicName0;
		return topicNameStr;
	}

}
