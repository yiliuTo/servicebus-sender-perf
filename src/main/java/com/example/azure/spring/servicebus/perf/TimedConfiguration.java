package com.example.azure.spring.servicebus.perf;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimedConfiguration implements SmartLifecycle {
   @Bean
   public TimedAspect timedAspect(MeterRegistry registry) {
      return new TimedAspect(registry);
   }

   @Override
   public void start() {

   }

   @Override
   public void stop() {

   }

   @Override
   public boolean isRunning() {
      return false;
   }
}