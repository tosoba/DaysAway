package com.trm.daysaway.core.base

import kotlinx.datetime.LocalDate
import kotlinx.datetime.serializers.LocalDateIso8601Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer

object LocalDateListSerializer :
  KSerializer<List<LocalDate>> by ListSerializer(LocalDateIso8601Serializer)
