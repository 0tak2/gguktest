<script setup>
import { reactive } from 'vue';

const userid = reactive({value: 'guest'});

</script>

<template>
  <div>
    <input type="text" v-model="userid.value" placeholder="아무 아이디나 입력"><br>
    <router-link :to="{ name: 'test1', params: { id: userid.value }}">테스트 페이지로 가기</router-link>
  </div>
</template>