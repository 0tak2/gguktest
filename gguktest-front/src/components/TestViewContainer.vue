<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const msg = ref('미디어 관련 테스트 - 조회');
const resp = ref('');
const route = useRoute();

onMounted(() => {
  console.log('mounted!');
})

function getGreeting() {
  axios.get('http://192.168.0.100:8080/test/greeting')
  .then((response) => {
    console.log(response);
    resp.value = response.data.message
  })
  .catch((response) => {
    console.error(response);
  });
}
</script>

<template>
  <router-link :to="{ name: 'test1', params: { id: route.params.id }}">업로드 페이지로 가기</router-link>

  <h1>{{ msg }}</h1>
  <input v-model="msg"><br>
  <button @click="getGreeting">테스트 접속 요청</button>
  <div>서버의 응답: {{resp}}</div>

  <section>
    <h2>이미지</h2>
    <img :src="`http://192.168.0.100:8080/test/resources/image/${route.params.id}.jpg`">
  </section>

  <section>
    <h2>영상 전송</h2>
    <video :src="`http://192.168.0.100:8080/test/resources/video/${route.params.id}.MOV`" autoplay loop muted playsinline></video>
    <video :src="`http://192.168.0.100:8080/test/resources/video/${route.params.id}.mp4`" autoplay loop muted playsinline></video>
  </section>
</template>

<style>
img {
  max-width: 200px;
}
video {
  display: block;
  max-width: 200px;
}
</style>