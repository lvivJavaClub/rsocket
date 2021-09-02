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

import org.reactivestreams.Publisher;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class SchoolCoursesController {

    private final RSocketRequester rSocketRequester;

    public SchoolCoursesController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @GetMapping("/course")
    public Flux<SchoolCourse> schoolCourses(@RequestParam(value = "count", defaultValue = "3") int count) {
        return rSocketRequester
                .route("schoolCourses")
                .retrieveFlux(SchoolCourse.class)
                .take(count);
    }

    @PostMapping("/course")
    public Mono<SchoolCourse> postSchoolCourse(@RequestBody String courseName) {
        return rSocketRequester
                .route("schoolCourseRequest")
                .data(SchoolCourse.builder().name(courseName).id(1).length(3).build())
                .retrieveMono(SchoolCourse.class);
    }

    @GetMapping("/course-test")
    public void schoolCoursesTest() {
        rSocketRequester
                .route("schoolCourses")
                .retrieveFlux(SchoolCourse.class)
                .subscribe(System.out::println);
    }
}
