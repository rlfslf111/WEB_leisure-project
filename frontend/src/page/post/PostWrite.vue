<template>
  <div class="p-0">
    <!-- 쓴 글이 없을 때 -->
    <div class="mt-5" v-if="wposts.length <= 0">
      <i class="fas fa-surprise mt-5 mr-2"></i>
      <span style="font-weight:bold">등록한 상품이 없습니다.</span>
      <i class="fas fa-surprise ml-2"></i>
      <br />
      <button
        @click="gocreate"
        class="btn mt-2 mb-5"
        style="font-weight:bold; color:white; background-color:RGB(134, 165, 212); border-radius:7px"
      >
        <i class="fas fa-heart mr-2"></i>상품 등록하러 가기
      </button>
    </div>

    <!-- 쓴 글이 있을 때 -->

    <!-- 웹버전 -->
    <div class="Webtable d-none d-sm-block">
      <table class="table" v-if="wposts.length > 0">
        <thead class="thead" style="background:RGB(134, 165, 212); color:white;">
          <tr>
            <th style="width:10%; white-space: nowrap;">No</th>
            <th style="width:20%; white-space: nowrap;">이미지</th>
            <th style="width:40%; white-space: nowrap;">제목</th>
            <th style="width:15%; white-space: nowrap;">작성일</th>
            <th style="width:15%; white-space: nowrap;">바로가기</th>
          </tr>
        </thead>
        <tr id="tt" v-for="(post, index) in wposts" :key="index">
          <td>{{((wpage - 1) * 8 ) + index + 1}}</td>
          <td>
            <img
              :src="makeimgurl(post.imgurl)"
              v-if="post.imgurl"
              style="width: 100px; height: 100px;"
            />
          </td>
          <td
            style="font-weight:bold;text-overflow:ellipsis; overflow: hidden; white-space: nowrap;"
          >{{ post.title }}</td>
          <td
            style="font-weight:bold;text-overflow:ellipsis; overflow: hidden; white-space: nowrap;"
          >{{ createdate(post.createDate) }}</td>
          <td>
            <button
              class="postwrite-button"
              @click="getdetail(post.pid)"
              style="font-weight:bold;"
            >바로가기</button>
          </td>
        </tr>
      </table>
      <br />

      <!-- paging -->
      <b-pagination
        class="mt-5 mb-0"
        v-if="wtotalPage > 10"
        v-model="wpage"
        :total-rows="wtotalPage"
        pills
        :per-page="10"
      ></b-pagination>
    </div>

    <!-- 모바일버전 > 이미지 들어오면 img에 :src="makeimgurl(post.imgurl)" v-if="post.imgurl" 수정해야함 -->
    <div class="MoblieCard d-block d-sm-none d-md-none">
      <div
        class="col-12 col-sm-12 col-md-3 card-deck"
        style="margin:auto 0; padding:0 30px;"
        v-for="(post, index) in wposts"
        :key="index"
      >
        <div class="card mb-3 profile-post mr-0 ml-0" style="cursor:pointer;">
          <div class="card-body" style="padding: 0;" @click="getdetail(post.pid)">
            <img
              v-if="post.imgurl"
              :src="makeimgurl(post.imgurl)"
              style="width: 100%; height: 15rem;"
            />
            <div class="col-md-12 p-0">
              <div class="card-body" style="padding: 5px;">
                <h5
                  style="text-overflow:ellipsis; overflow: hidden; white-space: nowrap; font-weight:bold;font-family: 'Nanum Gothic', sans-serif;"
                >{{ post.title }}</h5>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

const baseURL = process.env.VUE_APP_BACKURL;

export default {
  data() {
    return {
      wposts: [],
      type: "all",
      wtotalPage: 0,
      wpage: 1,
      email: "",
    };
  },
  created() {
    this.authUser();
  },
  methods: {
    postCount() {
      axios
        .get(`${baseURL}/post/count/mypost/${this.email}`)
        .then((res) => {
          this.wtotalPage = res.data;
          this.checkPage();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    authUser() {
      axios
        .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
        .then((res) => {
          this.email = res.data.email;
          this.postCount();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    makeimgurl(imgurl) {
      var url = "../../../contents/" + imgurl;
      return url;
    },
    checkPage() {
      axios
        .get(`${baseURL}/post/mypost/${this.email}/${this.wpage}`)
        .then((res) => {
          this.wposts = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    createdate(date) {
      var cd = date + "";
      return cd.substring(0, 10);
    },
    getdetail(pid) {
      scroll(0, 0);
      this.$router.push(`/posts/${pid}`);
    },
    gocreate() {
      scroll(0, 0);
      this.$router.push("/postcreate");
    },
  },
  watch: {
    wpage: function (v) {
      this.checkPage();
    },
  },
};
</script>

<style scoped>
.postwrite-button {
  border: none;
  background: none;
  color: blue;
}

.postwrite-button:focus {
  outline: none;
}

.postwrite-button:hover {
  color: red;
}
</style>