<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const msg = ref('미디어 관련 테스트 - 업로드');
const resp = ref('');
const previewImg = ref(null);
const previewVid = ref(null);
const route = useRoute();
const imgFile = ref(null);
const vidFile = ref(null);

onMounted(() => {
  console.log('mounted!');
})

function uploadImg() {
  axios.postForm(
    `http://192.168.0.100:8080/test/${route.params.id}/image`,
    {
      file: imgFile.value,
    }
  )
  .then((result) => {
    console.log(result);
    alert("이미지 업로드에 성공했습니다.");
  })
  .catch((result) => {
    console.error(result);
  });
}

function uploadVid() {
  axios.postForm(
    `http://192.168.0.100:8080/test/${route.params.id}/video`,
    {
      file: vidFile.value,
    }
  )
  .then((result) => {
    console.log(result);
    alert("비디오 업로드에 성공했습니다.");
  })
  .catch((result) => {
    console.error(result);
  });
}

function onChangeFile(e) {
  readUrl(e)

  const input = e.target;
  const fileType = input.files[0].type.split('/')[0];

  const file = fileType === 'image' ? imgFile : vidFile;
  if (input.files && input.files[0]) {
    file.value = input.files[0];
  }
}

function readUrl(e) {
  const input = e.target;
  const fileType = input.files[0].type.split('/')[0];
  const previewArea = fileType === 'image' ? previewImg : previewVid;
  if (input.files && input.files[0]) {
    var reader = new FileReader();
    reader.onload = function(e) {
      previewArea.value.src = e.target.result;
    };
    reader.readAsDataURL(input.files[0]);
  } else {
    previewArea.value.src = "";
  }
}

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

// Recorder.js
let URL = window.URL || window.webkitURL;

let gumStream;              //stream from getUserMedia()
let rec;                    //Recorder.js object
let input;                  //MediaStreamAudioSourceNode we'll be recording

// shim for AudioContext when it's not avb.
let AudioContext = window.AudioContext || window.webkitAudioContext;
let audioContext //new audio context to help us record

function startRecording() {
    console.log("recordButton clicked");

    navigator.mediaDevices.getUserMedia({audio: true, video: false}).then(function(stream) {
        console.log("getUserMedia() success, stream created, initializing Recorder.js ...");

        audioContext = new AudioContext({sampleRate: 16000});

        // assign to gumStream for later use
        gumStream = stream;

        // use the stream
        input = audioContext.createMediaStreamSource(stream);

        // Create the Recorder object and configure to record mono sound (1 channel) Recording 2 channels will double the file size
        // eslint-disable-next-line
        rec = new Recorder(input, {numChannels: 1})

        //start the recording process
        rec.record()

        console.log("Recording started");

    }).catch(function(err) {
        //enable the record button if getUserMedia() fails
        console.error(err);
    });
}

function stopRecording() {
    console.log("stopButton clicked");

    //tell the recorder to stop the recording
    rec.stop(); //stop microphone access
    gumStream.getAudioTracks()[0].stop();

    //create the wav blob and pass it on to createDownloadLink
    rec.exportWAV(createDownloadLink);
}

function createDownloadLink(blob) {
    let url = URL.createObjectURL(blob);
    let au = document.createElement('audio');
    let li = document.createElement('li');
    let link = document.createElement('a');

    //name of .wav file to use during upload and download (without extension)
    let filename = new Date().toISOString();

    //add controls to the <audio> element
    au.controls = true;
    au.src = url;

    //save to disk link
    link.href = url;
    link.download = filename+".wav"; //download forces the browser to download the file using the  filename
    link.innerHTML = "Save to disk";

    //add the new audio element to li
    li.appendChild(au);
    
    //add the filename to the li
    li.appendChild(document.createTextNode(filename+".wav "))

    //add the save to disk link to li
    li.appendChild(link);

    //add the li element to the ol
    // recordingsList.appendChild(li);
}
</script>

<template>
  <router-link :to="{ name: 'test2', params: { id: route.params.id }}">조회 페이지로 가기</router-link>

  {{ route.params.id }}
  <h1>{{ msg }}</h1>
  <input v-model="msg"><br>
  <button @click="getGreeting">테스트 접속 요청</button>
  <div>서버의 응답: {{resp}}</div>

  <section>
    <h2>모바일 - 이미지 전송</h2>
    <input type="file" accept="image/*" capture="environment" @change="onChangeFile">
    <img ref="previewImg"><br>
    <button @click="uploadImg">전송</button>
  </section>

  <section>
    <h2>모바일 - 영상 전송</h2>
    <input type="file" accept="video/*" capture="environment" @change="onChangeFile">
    <video ref="previewVid"></video><br>
    <button @click="uploadVid">전송</button>
  </section>

  <section>
    <h2>녹음 음성 전송</h2>
    <button @click="startRecording">녹음 시작</button>
    <button @click="stopRecording">녹음 종료</button>
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