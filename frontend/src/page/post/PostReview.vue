<template>
  <div>
    <!-- 등록한 후기가 없을때 -->
    <div class="mt-5 mb-5" v-if="reviews.length <= 0">
      <i class="fas fa-surprise mt-5 mr-2"></i
      ><span style="font-weight:bold">등록한 후기가 없습니다.</span
      ><i class="fas fa-surprise ml-2"></i>
      <br />
      <button
        @click="goPost"
        class="btn mt-2 mb-4"
        style="font-weight:bold; color:white; background-color:RGB(134, 165, 212); border-radius:7px"
      >
        <i class="fas fa-edit mr-2"></i>후기 남기러 가기
      </button>
    </div>
    <!-- 등록한 후기가 있을 때 -->
    <div class="row justify-content-left" v-if="reviews.length > 0">
      <div
        v-for="(slide, index) in reviews"
        :key="index"
        class="col-12 col-sm-6 col-md-3 p-3"
      >
        <!-- <div class="card mb-3 profile-post mr-0 ml-0"> -->
        <div
          data-toggle="modal"
          data-target="#postReviewModal"
          @click="bringReviewData(slide)"
          class="card-body"
          style="padding: 0 20px; cursor: pointer;"
        >
          
          <!-- img 보여주기 -->
          <img
            v-if="slide.img"
            class="review-img"
            :src="makeimgurl(slide.img)"
            :alt="slide.title"
            style="height:9rem; border-top-right-radius:5px; border-top-left-radius:5px;"
          />
          <img
            v-if="!slide.img"
            class="review-img"
            src="../../assets/img/noimage.jpg"
            style="height:8rem;"
          />
          <!-- 프로필 보여주기 -->
          <div class="d-flex justify-content-start">
            <img
              class="user-img d-flex m-3"
              v-if="slide.proimg"
              :src="makeimgurl(slide.proimg)"
              style="border-radius:70px;"
            />
            <div class="mt-2 mr-4">
              <div class="d-flex">
                <small
                  class="d-flex align-items-center"
                  style="font-weight:bold"
                  >{{ slide.nickname }}</small
                >
                <br />
              </div>
              <div class="d-flex">
                <i
                  class="fas fa-star"
                  style="color:Salmon"
                  v-for="i in slide.star"
                  :key="i.id"
                ></i>
              </div>
              <div class="d-flex align-items-end mt-1">
                <small style="font-weight:bold; white-space:nowrap">{{
                  datecut(slide.createDate)
                }}</small>
              </div>
            </div>
          </div>
          <!-- 제목 -->
          <div class="d-flex mt-1 justify-content-start">
            <span style="font-weight:bold; text-align:justify;">{{ slide.title }}</span>
          </div>

          
        </div>
        <!-- </div> -->
      </div>
    </div>
      <PostReviewDetailModal :reviewDetail="reviewDetail" />

    <!-- paging -->
    <b-pagination class="mt-5 mb-0" v-if="rtotalPage > 8" v-model="rpage" :total-rows="rtotalPage" pills :per-page="8"></b-pagination>
  </div>
</template>

<script>
import axios from "axios";

const baseURL = process.env.VUE_APP_BACKURL;

import PostReviewDetailModal from "./PostReviewDetailModal.vue";

export default {
  components: {
    PostReviewDetailModal,
  },
  data() {
    return {
      reviews: [],
      email: "",
      reviewDetail: [],
      rpage: 1,
      rtotalPage: 0,
    };
  },
  methods: {
    authUser() {
      axios
        .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
        .then((response) => {
          this.email = response.data.email;
          this.countReview();
        })
        .catch((err) => {
          console.log(err.response);
        });
    },
     makeimgurl(imgurl) {
      var url = "../../../contents/"+imgurl;
      return url;
    },
    countReview() {
      axios
        .get(`${baseURL}/review/count/listbyemail/${this.email}`)
        .then((response) => {
          this.rtotalPage = response.data;
          this.fetchReview();
        })
        .catch((error) => {
          console.log(error);
        });
    },
    fetchReview() {
      axios
        .get(`${baseURL}/review/listbyemail/${this.email}/${this.rpage}`)
        .then((response) => {
          this.reviews = response.data;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    datecut(date) {
      var tempdatecut = date + "";
      return tempdatecut.substring(0, 10);
    },
    bringReviewData(reviewData) {
      this.reviewDetail = reviewData;
    },
    goPost() {
      this.$router.push('/posts')
    },
  },
  created() {
    this.authUser();
  },
  watch: {
    rpage: function(v) {
      this.fetchReview();
    }
  }
};
</script>

<style scoped>
.review-img {
  width: 100%;
  height: 8rem;
  box-shadow: 5px 5px 5px rgba(0, 0, 0, 0.15);
}
.user-img {
  width: 50px;
  height: 50px;
}

.card-body:hover {
  transform: scale(1.05);
  transition: all 0.3s ease-in-out;
  cursor: pointer;
}
           
</style>
