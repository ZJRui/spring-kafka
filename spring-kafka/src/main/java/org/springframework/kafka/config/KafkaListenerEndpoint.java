/*
 * Copyright 2002-2022 the original author or authors.
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

package org.springframework.kafka.config;

import java.util.Collection;
import java.util.Properties;
import java.util.regex.Pattern;

import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.TopicPartitionOffset;
import org.springframework.kafka.support.converter.MessageConverter;
import org.springframework.lang.Nullable;

/**
 * Model for a Kafka listener endpoint. Can be used against a
 * {@link org.springframework.kafka.annotation.KafkaListenerConfigurer
 * KafkaListenerConfigurer} to register endpoints programmatically.
 *
 * @author Stephane Nicoll
 * @author Gary Russell
 */
public interface KafkaListenerEndpoint {

	/**
	 * Kafka侦听器端点模型。
	 * 是否可以用于kafkalistenerconfigururer以编程方式注册端点
	 */

	/**
	 * Return the id of this endpoint.
	 * @return the id of this endpoint. The id can be further qualified
	 * when the endpoint is resolved against its actual listener
	 * container. //这个端点的id。当针对端点的实际侦听器容器解析端点时，可以进一步限定该id。
	 * @see KafkaListenerContainerFactory#createListenerContainer
	 */
	@Nullable
	String getId();//这个端点的id。当针对端点的实际侦听器容器解析端点时，可以进一步限定该id。

	/**
	 * Return the groupId of this endpoint - if present, overrides the
	 * {@code group.id} property of the consumer factory.
	 * @return the group id; may be null.
	 * @since 1.3
	 */
	@Nullable
	String getGroupId();//返回此端点的groupId—如果存在，则覆盖该组。消费工厂的Id属性。

	/**
	 * Return the group of this endpoint or null if not in a group.
	 * @return the group of this endpoint or null if not in a group.
	 */
	@Nullable
	String getGroup();//返回此端点所在的组，如果不在组中则返回null。

	/**
	 * Return the topics for this endpoint.
	 * @return the topics for this endpoint.
	 */
	Collection<String> getTopics();

	/**
	 * Return the topicPartitions for this endpoint.
	 * @return the topicPartitions for this endpoint.
	 * @since 2.3
	 */
	@Nullable
	TopicPartitionOffset[] getTopicPartitionsToAssign();

	/**
	 * Return the topicPattern for this endpoint.
	 * @return the topicPattern for this endpoint.
	 */
	@Nullable
	Pattern getTopicPattern();


	/**
	 * Return the client id prefix for the container; it will be suffixed by
	 * '-n' to provide a unique id when concurrency is used.
	 * @return the client id prefix.
	 * @since 2.1.1
	 */
	@Nullable
	String getClientIdPrefix();//返回容器的客户端id前缀;当使用并发时，它将以'-n'作为后缀，以提供一个唯一的id。

	/**
	 * Return the concurrency for this endpoint's container.
	 * @return the concurrency.
	 * @since 2.2
	 */
	@Nullable
	Integer getConcurrency();//返回此端点容器的并发性。

	/**
	 * Return the autoStartup for this endpoint's container.
	 * @return the autoStartup.
	 * @since 2.2
	 */
	@Nullable
	Boolean getAutoStartup();//返回此端点容器的autoStartup。

	/**
	 * Get the consumer properties that will be merged with the consumer properties
	 * provided by the consumer factory; properties here will supersede any with the same
	 * name(s) in the consumer factory.
	 * {@code group.id} and {@code client.id} are ignored.
	 *
	 * <p>
	 *     获取将与消费者工厂提供的消费者属性合并的消费者属性;此处的属性将取代消费者工厂中具有相同
	 *     名称的任何属性。组。id和客户端。id被忽略。
	 * </p>
	 *
	 * @return the properties.
	 * @since 2.1.4
	 * @see org.apache.kafka.clients.consumer.ConsumerConfig
	 * @see #getGroupId()
	 * @see #getClientIdPrefix()
	 */
	@Nullable
	default Properties getConsumerProperties() {
		return null;
	}

	/**
	 * Setup the specified message listener container with the model
	 * defined by this endpoint.
	 * <p>This endpoint must provide the requested missing option(s) of
	 * the specified container to make it usable. Usually, this is about
	 * setting the {@code queues} and the {@code messageListener} to
	 * use but an implementation may override any default setting that
	 * was already set.
	 *
	 * <p>
	 * 使用此端点定义的模型设置指定的消息侦听器容器。
	 * 该端点必须提供所请求的指定容器的缺失选项，以使其可用。通常，
	 * 这是关于设置要使用的队列和messageListener，但实现可能会覆盖已经设置的任何默认设置。
	 * </p>
	 *
	 * @param listenerContainer the listener container to configure
	 * @param messageConverter the message converter - can be null
	 */
	void setupListenerContainer(MessageListenerContainer listenerContainer,
			@Nullable MessageConverter messageConverter);

	/**
	 * When true, {@link Iterable} return results will be split into discrete records.
	 * @return true to split.
	 * @since 2.3.5
	 */
	boolean isSplitIterables();//当为true时，Iterable返回的结果将被分割成离散的记录。

	/**
	 * Get the listener info to insert in the record header.
	 * //获取要插入到记录头中的侦听器信息。
	 * @return the info.
	 * @since 2.8.4
	 */
	@Nullable
	default byte[] getListenerInfo() {
		return null;
	}

}
