<template>
  <div class="container col-sm-12 col-md-12 col-lg-12 p-0">
    <div class="post-img" style="display:block;"></div>
    <div class="post mt-5">
      <h1 class="mainTitle">Latest News Top 10</h1>
      <div class="container col-md-8">
        <div v-for="(news, index) in newsData" :key="index" class="newsForm" @click="goNews(news.newsUrl)">
          <div class="col-md-4 newsRoom">
            <h6>
              <small>
                {{ news.newsroom }}
              </small>
            </h6>
          </div>
          <div class="col-md-12 newsTitle">
            <h4>
              {{ news.title }}
            </h4>
          </div>
          <div class="col-md-12 newsContent">
            {{ news.content }}
          </div>
          <div class="newsDate">
            {{ news.createDate }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

const baseURL = process.env.VUE_APP_BACKURL;

export default {
  data() {
    return {
      newsData: [],
    };
  },
  methods: {
    goNews(url) {
      window.location.href = url;
    },
  },
  created() {
    axios
      .get(`${baseURL}/news/list`)
      .then((res) => {
        this.newsData = res.data;
      })
      .catch((err) => {
        console.log(err);
      });
  },
};
</script>

<style scoped>
.mainTitle {
  text-shadow: 2px 2px 3px rgba(113, 169, 253, 0.699);
  margin-bottom: 50px;
  font-family: "Lucida Console", Courier, monospace;
}
.newsForm {
  border: 2px solid black;
  border-radius: 20px;
  margin-bottom: 50px;
  padding: 30px;
  text-align: left;
}
.newsForm:hover {
  background-color: rgba(0, 0, 0, 0.9);
  color: white;
  cursor: pointer;
}
.newsDate {
  text-align:right !important;
}
.newsContent {
  margin: 20px 0 5px 0;
}
.newsRoom {
  margin-bottom: 10px ;
}
</style>
