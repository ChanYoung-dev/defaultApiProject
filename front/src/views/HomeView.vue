<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
// const posts = [
//   { id:1, title: "제목1", content: "내용1" },
//   { id:2, title: "제목2", content: "내용2" },
//   { id:3, title: "제목3", content: "내용3" },
//   { id:4, title: "제목4", content: "내용4" },
// ];

const posts = ref([]);

axios.get("/my-backend-api/posts?page=1&size=5").then((response) =>{
  console.log(response);
  response.data.forEach((r: any) => {
    posts.value.push(r);
      }
  );
});


</script>

<template>
  <li v-for="post in posts" :key="post.id">
    <div>
      {{ post.title }}
    </div>

    <div>
      {{ post.content }}
    </div>

  </li>

</template>

<style scoped>
li {
  margin-bottom: 1rem;
}

li:last-child {
  margin-bottom: 0;
}

</style>