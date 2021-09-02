/*
 * Copyright (c) 2008-2021, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lvivjavaclub.rsocket;

import io.rsocket.transport.netty.client.TcpClientTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import reactor.util.retry.Retry;

import java.time.Duration;

@Configuration
public class ClientConfig {

    @Bean
    public RSocketStrategies rSocket() {
        return RSocketStrategies.builder()
                .encoders(encoders -> encoders.add(new Jackson2JsonEncoder()))
                .decoders(decoders -> decoders.add(new Jackson2JsonDecoder()))
                .build();
    }

    @Bean
    public RSocketRequester rSocketRequester(RSocketRequester.Builder builder) {
        return builder
                .rsocketConnector(connector -> connector.reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
                .dataMimeType(MediaType.APPLICATION_JSON)
                .transport(TcpClientTransport.create(7000));
    }
}
