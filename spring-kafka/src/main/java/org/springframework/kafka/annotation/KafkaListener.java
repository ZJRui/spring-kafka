/*
 * Copyright 2016-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.kafka.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.messaging.handler.annotation.MessageMapping;

/**
 * Annotation that marks a method to be the target of a Kafka message listener on the
 * specified topics.
 *
 * The {@link #containerFactory()} identifies the
 * {@link org.springframework.kafka.config.KafkaListenerContainerFactory
 * KafkaListenerContainerFactory} to use to build the Kafka listener container. If not
 * set, a <em>default</em> container factory is assumed to be available with a bean name
 * of {@code kafkaListenerContainerFactory} unless an explicit default has been provided
 * through configuration.
 *
 * <p>
 * Processing of {@code @KafkaListener} annotations is performed by registering a
 * {@link KafkaListenerAnnotationBeanPostProcessor}. This can be done manually or, more
 * conveniently, through {@link EnableKafka} annotation.
 *
 * <p>
 * Annotated methods are allowed to have flexible signatures similar to what
 * {@link MessageMapping} provides, that is
 * <ul>
 * <li>{@link org.apache.kafka.clients.consumer.ConsumerRecord} to access to the raw Kafka
 * message</li>
 * <li>{@link org.springframework.kafka.support.Acknowledgment} to manually ack</li>
 * <li>{@link org.springframework.messaging.handler.annotation.Payload @Payload}-annotated
 * method arguments including the support of validation</li>
 * <li>{@link org.springframework.messaging.handler.annotation.Header @Header}-annotated
 * method arguments to extract a specific header value, defined by
 * {@link org.springframework.kafka.support.KafkaHeaders KafkaHeaders}</li>
 * <li>{@link org.springframework.messaging.handler.annotation.Headers @Headers}-annotated
 * argument that must also be assignable to {@link java.util.Map} for getting access to
 * all headers.</li>
 * <li>{@link org.springframework.messaging.MessageHeaders MessageHeaders} arguments for
 * getting access to all headers.</li>
 * <li>{@link org.springframework.messaging.support.MessageHeaderAccessor
 * MessageHeaderAccessor} for convenient access to all method arguments.</li>
 * </ul>
 *
 * <p>When defined at the method level, a listener container is created for each method.
 * The {@link org.springframework.kafka.listener.MessageListener} is a
 * {@link org.springframework.kafka.listener.adapter.MessagingMessageListenerAdapter},
 * configured with a {@link org.springframework.kafka.config.MethodKafkaListenerEndpoint}.
 *
 * <p>When defined at the class level, a single message listener container is used to
 * service all methods annotated with {@code @KafkaHandler}. Method signatures of such
 * annotated methods must not cause any ambiguity such that a single method can be
 * resolved for a particular inbound message. The
 * {@link org.springframework.kafka.listener.adapter.MessagingMessageListenerAdapter} is
 * configured with a
 * {@link org.springframework.kafka.config.MultiMethodKafkaListenerEndpoint}.
 *
 * @author Gary Russell
 * @author Venil Noronha
 *
 * @see EnableKafka
 * @see KafkaListenerAnnotationBeanPostProcessor
 * @see KafkaListeners
 */
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@MessageMapping
@Documented
@Repeatable(KafkaListeners.class)
public @interface KafkaListener {
	/**
	 *
	 * 将一个方法标记为指定主题上Kafka消息监听器的目标的注释。containerFactory()标识用于构建Kafka侦听
	 * 器容器的KafkaListenerContainerFactory。如果没有设置，默认的容器工厂将被假定为可用的bean名称
	 * 为kafkaListenerContainerFactory，除非配置中已经提供了显式的默认值。
	 * @KafkaListener注释的处理是通过注册一个KafkaListenerAnnotationBeanPostProcessor来
	 * 执行的。这可以手动完成，也可以更方便地通过EnableKafka注释完成。
	 * 也就是说，允许带注释的方法具有与MessageMapping提供的类似的灵活签名
	 *
	 *
	 * -------------
	 *
	 *
	 */

	/**
	 * The unique identifier of the container for this listener.
	 * <p>If none is specified an auto-generated id is used.
	 * <p>Note: When provided, this value will override the group id property
	 * in the consumer factory configuration, unless {@link #idIsGroup()}
	 * is set to false or {@link #groupId()} is provided.
	 * <p>SpEL {@code #{...}} and property place holders {@code ${...}} are supported.
	 *
	 *
	 * <p>
	 * 此侦听器的容器的唯一标识符。
	 * 如果不指定，则使用自动生成的id。
	 * 注意:当提供此值时，该值将覆盖消费者工厂配置中的组id属性，
	 * 除非将idIsGroup()设置为false或提供groupId()。
	 * ? #{…}和属性位置持有者${…支持}。
	 *
	 * ----------
	 * 如果  没有设置 id，那么 @kafkaListener 注解 解析成 MethodKakfaListenerEndpoint 这个endpoint对象的id 就会通过
	 * KafkaListenerAnnotationBeanPostProcessor#getEndpointId 方法自动生成
	 *
	 * </p>
	 *
	 * @return the {@code id} for the container managing for this endpoint.
	 * @see org.springframework.kafka.config.KafkaListenerEndpointRegistry#getListenerContainer(String)
	 */
	String id() default "";

	/**
	 * The bean name of the {@link org.springframework.kafka.config.KafkaListenerContainerFactory}
	 * to use to create the message listener container responsible to serve this endpoint.
	 * <p>
	 * If not specified, the default container factory is used, if any. If a SpEL
	 * expression is provided ({@code #{...}}), the expression can either evaluate to a
	 * container factory instance or a bean name.
	 *
	 * <p>
	 *  kafkalistenercontainerfactory的bean名，用来创建负责服务于此端点的消息侦听器容器。
	 * 如果没有指定，则使用默认的容器工厂(如果有的话)。如果提供了一个SpEL表达式(#{…})，
	 * 该表达式可以求值为容器工厂实例或bean名称。
	 * </p>
	 *
	 * @return the container factory bean name.
	 */
	String containerFactory() default "";

	/**
	 * The topics for this listener.
	 * The entries can be 'topic name', 'property-placeholder keys' or 'expressions'.
	 * An expression must be resolved to the topic name.
	 * This uses group management and Kafka will assign partitions to group members.
	 * <p>
	 * Mutually exclusive with {@link #topicPattern()} and {@link #topicPartitions()}.
	 *
	 * <p>
	 *     此侦听器的主题。条目可以是“主题名称”、“属性-占位符键”或“表达式”。表达式必须解析为主题名称。
	 *     这使用组管理，Kafka将分配分区给组成员。
	 * 与topicPattern()和topicPartitions()互斥。
	 * </p>
	 *
	 * @return the topic names or expressions (SpEL) to listen to.
	 */
	String[] topics() default {};

	/**
	 * The topic pattern for this listener. The entries can be 'topic pattern', a
	 * 'property-placeholder key' or an 'expression'. The framework will create a
	 * container that subscribes to all topics matching the specified pattern to get
	 * dynamically assigned partitions. The pattern matching will be performed
	 * periodically against topics existing at the time of check. An expression must
	 * be resolved to the topic pattern (String or Pattern result types are supported).
	 * This uses group management and Kafka will assign partitions to group members.
	 * <p>
	 * Mutually exclusive with {@link #topics()} and {@link #topicPartitions()}.
	 *
	 * <p>
	 *     此侦听器的主题模式。条目可以是“主题模式”、“属性-占位符键”或“表达式”。
	 *     框架将创建一个容器，该容器订阅与指定模式匹配的所有主题，以获得动态分配的分区。
	 *     模式匹配将定期针对检查时存在的主题执行。表达式必须解析为主题模式(支持字符串或模式结果类型)。
	 *     这使用组管理，Kafka将分配分区给组成员。
	 * 与topics()和topicPartitions()互斥。
	 * </p>
	 *
	 * @return the topic pattern or expression (SpEL).
	 *
	 *
	 * @see org.apache.kafka.clients.CommonClientConfigs#METADATA_MAX_AGE_CONFIG
	 */
	String topicPattern() default "";

	/**
	 * The topicPartitions for this listener when using manual topic/partition
	 * assignment.
	 * <p>
	 * Mutually exclusive with {@link #topicPattern()} and {@link #topics()}.
	 * <p>
	 *     当使用手动主题/分区分配时，此侦听器的topicPartitions。
	 * 与topicPattern()和topics()互斥。
	 * </p>
	 * @return the topic names or expressions (SpEL) to listen to.
	 */
	TopicPartition[] topicPartitions() default {};

	/**
	 * If provided, the listener container for this listener will be added to a bean with
	 * this value as its name, of type {@code Collection<MessageListenerContainer>}. This
	 * allows, for example, iteration over the collection to start/stop a subset of
	 * containers. The {@code Collection} beans are deprecated as of version 2.7.3 and
	 * will be removed in 2.8. Instead, a bean with name {@code containerGroup + ".group"}
	 * and type {@link org.springframework.kafka.listener.ContainerGroup} should be used
	 * instead.
	 * <p>
	 * SpEL {@code #{...}} and property place holders {@code ${...}} are supported.
	 *
	 * <p>
	 *     如果提供，这个侦听器的侦听器容器将被添加到一个bean中，该bean的名称为这个值，
	 *     类型为Collection&lt;MessageListenerContainer&gt;例如，这允许对集合进行迭代，
	 *     以启动/停止容器的子集。收集bean在2.7.3版本中已弃用，并将在2.8版本中删除。
	 *     相反，使用名称为containerGroup +的bean。并且应该使用org.springframework.kafka.listener.
	 *     ContainerGroup类型。
	 * ? #{…}和属性位置持有者${…支持}。
	 * 返回:
	 * 组的bean名称。
	 * </p>
	 *
	 * @return the bean name for the group.
	 */
	String containerGroup() default "";

	/**
	 * Set an {@link org.springframework.kafka.listener.KafkaListenerErrorHandler} bean
	 * name to invoke if the listener method throws an exception. If a SpEL expression is
	 * provided ({@code #{...}}), the expression can either evaluate to a
	 * {@link org.springframework.kafka.listener.KafkaListenerErrorHandler} instance or a
	 * bean name.
	 * @return the error handler.
	 * @since 1.3
	 */
	String errorHandler() default "";

	/**
	 * Override the {@code group.id} property for the consumer factory with this value
	 * for this listener only.
	 * <p>SpEL {@code #{...}} and property place holders {@code ${...}} are supported.
	 *
	 * <p>
	 *     覆盖。使用此值仅为该侦听器的消费者工厂Id属性。
	 * ? #{…}和属性位置持有者${…支持}。
	 * </p>
	 * @return the group id.
	 * @since 1.3
	 */
	String groupId() default "";

	/**
	 * When {@link #groupId() groupId} is not provided, use the {@link #id() id} (if
	 * provided) as the {@code group.id} property for the consumer. Set to false, to use
	 * the {@code group.id} from the consumer factory.
	 * <p>
	 *     当没有提供groupId时，使用id(如果提供)作为组。使用者的Id属性。设置为false，则使用组。我是消费者工厂的。
	 * </p>
	 * @return false to disable.
	 * @since 1.3
	 */
	boolean idIsGroup() default true;

	/**
	 * When provided, overrides the client id property in the consumer factory
	 * configuration. A suffix ('-n') is added for each container instance to ensure
	 * uniqueness when concurrency is used.
	 * <p>SpEL {@code #{...}} and property place holders {@code ${...}} are supported.
	 *
	 * <p>
	 *     当提供时，重写消费者工厂配置中的客户端id属性。为每个容器实例添加一个后缀('-n')，以确保使用并发时的唯一性。
	 * ? #{…}和属性位置持有者${…支持}。
	 * </p>
	 * @return the client id prefix.
	 * @since 2.1.1
	 */
	String clientIdPrefix() default "";

	/**
	 * A pseudo bean name used in SpEL expressions within this annotation to reference
	 * the current bean within which this listener is defined. This allows access to
	 * properties and methods within the enclosing bean.
	 * Default '__listener'.
	 * <p>
	 * Example: {@code topics = "#{__listener.topicList}"}.
	 * <p>
	 *     这个注释中的SpEL表达式中使用的伪bean名，用于引用在其中定义侦听器的当前bean。
	 *     这允许访问外围bean中的属性和方法。默认“__listener”。
	 * 例如:topics = "#{__listener.topicList}"。
	 * </p>
	 * @return the pseudo bean name.
	 * @since 2.1.2
	 */
	String beanRef() default "__listener";

	/**
	 * Override the container factory's {@code concurrency} setting for this listener. May
	 * be a property placeholder or SpEL expression that evaluates to a {@link Number}, in
	 * which case {@link Number#intValue()} is used to obtain the value.
	 * <p>SpEL {@code #{...}} and property place holders {@code ${...}} are supported.
	 * <p>
	 *     为这个侦听器重写容器工厂的并发性设置。可以是一个属性占位符或求值为Number的SpEL表达式，在这种情况下使用Number. intvalue()来获取值。
	 * ? #{…}和属性位置持有者${…支持}。
	 * 返回:
	 * 并发性。
	 * </p>
	 * @return the concurrency.
	 * @since 2.2
	 */
	String concurrency() default "";

	/**
	 * Set to true or false, to override the default setting in the container factory. May
	 * be a property placeholder or SpEL expression that evaluates to a {@link Boolean} or
	 * a {@link String}, in which case the {@link Boolean#parseBoolean(String)} is used to
	 * obtain the value.
	 * <p>SpEL {@code #{...}} and property place holders {@code ${...}} are supported.
	 * <p>
	 *     设置为true或false，以覆盖容器工厂中的默认设置。可以是一个属性占位符或计算为布尔值或String值的SpEL表达式，在这种情况下使用Boolean. parseboolean (String)来获取值。
	 * ? #{…}和属性位置持有者${…支持}。
	 * </p>
	 * @return true to auto start, false to not auto start.
	 * @since 2.2
	 */
	String autoStartup() default "";

	/**
	 * Kafka consumer properties; they will supersede any properties with the same name
	 * defined in the consumer factory (if the consumer factory supports property overrides).
	 * <p>
	 * <b>Supported Syntax</b>
	 * <p>The supported syntax for key-value pairs is the same as the
	 * syntax defined for entries in a Java
	 * {@linkplain java.util.Properties#load(java.io.Reader) properties file}:
	 * <ul>
	 * <li>{@code key=value}</li>
	 * <li>{@code key:value}</li>
	 * <li>{@code key value}</li>
	 * </ul>
	 * {@code group.id} and {@code client.id} are ignored.
	 * <p>SpEL {@code #{...}} and property place holders {@code ${...}} are supported.
	 * SpEL expressions must resolve to a {@link String}, a @{link String[]} or a
	 * {@code Collection<String>} where each member of the array or collection is a
	 * property name + value with the above formats.
	 * @return the properties.
	 * @since 2.2.4
	 * @see org.apache.kafka.clients.consumer.ConsumerConfig
	 * @see #groupId()
	 * @see #clientIdPrefix()
	 */
	String[] properties() default {};

	/**
	 * When false and the return type is an {@link Iterable} return the result as the
	 * value of a single reply record instead of individual records for each element.
	 * Default true. Ignored if the reply is of type {@code Iterable<Message<?>>}.
	 * @return false to create a single reply record.
	 * @since 2.3.5
	 */
	boolean splitIterables() default true;

	/**
	 * Set the bean name of a
	 * {@link org.springframework.messaging.converter.SmartMessageConverter} (such as the
	 * {@link org.springframework.messaging.converter.CompositeMessageConverter}) to use
	 * in conjunction with the
	 * {@link org.springframework.messaging.MessageHeaders#CONTENT_TYPE} header to perform
	 * the conversion to the required type. If a SpEL expression is provided
	 * ({@code #{...}}), the expression can either evaluate to a
	 * {@link org.springframework.messaging.converter.SmartMessageConverter} instance or a
	 * bean name.
	 *
	 * <p>
	 *     设置org.springframework.messaging.converter.SmartMessageConverter(例如org.springframewor
	 *     k.messaging.converter.CompositeMessageConverter)的bean名，以便与org.springframewo
	 *     rk.messaging.MessageHeaders一起使用。CONTENT_TYPE头来执行到所需类型的转换。
	 *     如果提供了一个SpEL表达式(#{…})，该表达式可以计算为org.springframework.messaging.converter.
	 *     SmartMessageConverter实例或bean名。
	 *
	 *
	 * </p>
	 * @return the bean name.
	 * @since 2.7.1
	 */
	String contentTypeConverter() default "";

	/**
	 * Override the container factory's {@code batchListener} property. The listener
	 * method signature should receive a {@code List<?>}; refer to the reference
	 * documentation. This allows a single container factory to be used for both record
	 * and batch listeners; previously separate container factories were required.
	 * @return "true" for the annotated method to be a batch listener or "false" for a
	 * record listener. If not set, the container factory setting is used. SpEL and
	 * property placeholders are not supported because the listener type cannot be
	 * variable.
	 * <P>
	 *     重写容器工厂的batchListener属性。监听器方法签名应该接收一个List&lt;?&gt;;请参阅参考文档。这允许一个容器工厂同时用于记录侦听器和批处理侦听器;以前需要单独的集装箱工厂。
	 * 返回:
	 * “true”表示带注释的方法是批处理侦听器，“false”表示记录侦听器。如果没有设置，则使用容器工厂设置。不支持拼写和属性占位符，因为侦听器类型不能是可变的。
	 * </P>
	 * @since 2.8
	 * @see Boolean#parseBoolean(String)
	 */
	String batch() default "";

	/**
	 * Set an {@link org.springframework.kafka.listener.adapter.RecordFilterStrategy} bean
	 * name to override the strategy configured on the container factory. If a SpEL
	 * expression is provided ({@code #{...}}), the expression can either evaluate to a
	 * {@link org.springframework.kafka.listener.adapter.RecordFilterStrategy} instance or
	 * a bean name.
	 * @return the error handler.
	 * @since 2.8.4
	 */
	String filter() default "";

	/**
	 * Static information that will be added as a header with key
	 * {@link org.springframework.kafka.support.KafkaHeaders#LISTENER_INFO}. This can be
	 * used, for example, in a
	 * {@link org.springframework.kafka.listener.RecordInterceptor},
	 * {@link org.springframework.kafka.listener.adapter.RecordFilterStrategy} or the
	 * listener itself, for any purposes.
	 * <p>
	 * SpEL {@code #{...}} and property place holders {@code ${...}} are supported, but it
	 * must resolve to a String or {@code byte[]}.
	 * <p>
	 * This header will be stripped out if an outbound record is created with the headers
	 * <P>
	 *     静态信息将被添加为头部，关键字为org.springframework.kafka.support kafkaheaders . listener_info。例如，可以在org.springframework.kafka.listener中使用。recordfilterstrategy，或者监听器本身。
	 * ? #{…}和属性位置持有者${…}支持，但它必须解析为String或byte[]。
	 * 如果使用输入记录的标头创建出站记录，则该标头将被删除。
	 * </P>
	 * from an input record.
	 * @return the info.
	 * @since 2.8.4
	 */
	String info() default "";

}
