/*
 * Copyright 2025 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.opensandbox.sandbox.infrastructure.adapters.converter

import com.alibaba.opensandbox.sandbox.domain.models.execd.executions.CommandStatus
import com.alibaba.opensandbox.sandbox.domain.models.execd.executions.RunCommandRequest
import com.alibaba.opensandbox.sandbox.api.models.execd.CommandStatusResponse as ApiCommandStatusResponse
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

object ExecutionConverter {
    fun RunCommandRequest.toApiRunCommandPayload(): JsonObject {
        return buildJsonObject {
            put("command", JsonPrimitive(command))
            if (background) {
                put("background", JsonPrimitive(background))
            }
            workingDirectory?.let { put("cwd", JsonPrimitive(it)) }
            timeout?.let { put("timeout", JsonPrimitive(it.inWholeMilliseconds)) }
            uid?.let { put("uid", JsonPrimitive(it)) }
            gid?.let { put("gid", JsonPrimitive(it)) }
            if (envs.isNotEmpty()) {
                put(
                    "envs",
                    buildJsonObject {
                        envs.forEach { (key, value) ->
                            put(key, JsonPrimitive(value))
                        }
                    },
                )
            }
        }
    }

    fun ApiCommandStatusResponse.toCommandStatus(): CommandStatus {
        return CommandStatus(
            id = id,
            content = content,
            running = running,
            exitCode = exitCode,
            error = error,
            startedAt = startedAt,
            finishedAt = finishedAt,
        )
    }
}
