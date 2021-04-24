<template>
  <div>
    <div v-if="!flag">
      <!-- <i class="far fa-surprise mr-1 mb-3"></i> -->
      등록된 후기가 없습니다.
      <!-- 처음으로 후기를 남겨보세요!<i class="far fa-surprise ml-1"></i> -->
    </div>
    <div v-if="flag" id="slider" class="slider" @mousemove="mouseMoving" @mouseout="stopDrag">
      <div class="slider-cards" :style="`transform: translate3d(${cardsX}px,0,0)`">
        <div
          @mousedown="startDrag"
          @mouseup="stopDrag"
          v-for="(slide, rvid) in slides"
          :key="rvid"
          class="slider-card"
        >
          <!-- img 보여주기 -->
          <img
            class="review-img"
            style="width:100%"
            v-if="slide.img"
            :src="makeimgurl(slide.img)"
            :alt="slide.title"
            draggable="false"
          />
          <img
            class="review-img"
            style="width:100%"
            v-if="!slide.img"
            src="../../assets/img/noimage.jpg"
            :alt="slide.title"
            draggable="false"
          />
          <!-- 프로필 보여주기 -->
          <div class="d-flex justify-content-between" style="border-bottom : 1px solid lightgray;">
            <img class="profile-img d-flex m-2" v-if="slide.proimg" :src="makeimgurl(slide.proimg)" />
            <img class="profile-img d-flex m-2" v-if="!slide.proimg" src="../../assets/img/noimage.jpg" />
            <div class="mt-2 mr-3">
              <div class="d-flex">
                <small class="d-flex align-items-center" style="font-weight:bold">{{slide.nickname}}</small>
                <br />
              </div>
              <div class="d-flex">
                <i
                  class="fas fa-star"
                  style="color:Salmon; font-size:0.7erm;"
                  v-for="i in slide.star"
                  :key="i.id"
                ></i>
              </div>
            </div>
            <!-- 날짜 및 수정 삭제 -->
            <div v-if="email == slide.email" class="mt-2">
              <div class="d-flex justify-content-end mr-3">
                <small
                  @click="update(slide)"
                  data-toggle="modal"
                  data-target="#reviewUpdate"
                  style="color:blue;"
                >
                  <i class="fas fa-wrench" title="수정"></i>
                </small>
                <small @click="reviewDelete(slide.rvid)" style="color:red">
                  <i class="fas fa-trash-alt ml-2" title="삭제"></i>
                </small>
              </div>
              <div class="d-flex align-items-end mt-1 mr-3" style="white-space: nowrap;">
                <small style="font-weight:bold;">{{datecut(slide.createDate)}}</small>
              </div>
            </div>
            <div v-if="email != slide.email" class="d-flex align-items-center mr-3" style="white-space: nowrap; margin-top:7%">
              <small style="font-weight:bold">{{datecut(slide.createDate)}}</small>
            </div>
          </div>
          <!-- 제목 -->
          <div class="d-flex ml-2 mr-2 mt-2">
            <span style="font-weight:bold; font-size:1rem; text-align:justify; font-family: 'Jua', sans-serif;">[{{slide.title}}]</span>
          </div>
          <!-- 내용 -->
          <div class="d-flex ml-2 mr-2">
            <small style="text-align:justify; font-family: 'Nanum Gothic', sans-serif;">{{slide.content}}</small>
          </div>
        </div>
      </div>
      <ReviewUpdate v-if="this.isUpdated" :reviewInfo="reviewInfo" @review-close="reviewClose" />
    </div>
  </div>
</template>

<script>
import axios from "axios";

const baseURL = process.env.VUE_APP_BACKURL;

import ReviewUpdate from "./ReviewUpdateModal.vue";

export default {
  components: {
    ReviewUpdate,
  },
  props: {
    pid: [Number, String],
  },
  data() {
    return {
      slides: [],
      selectedIndex: 0,
      dragging: false,
      initialMouseX: 0,
      initialCardsX: 0,
      cardsX: 0,
      email: "",
      reviewInfo: {},
      isUpdated: false,
      flag: true,
    };
  },
  methods: {
    authUser() {
      axios
        .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
        .then((response) => {
          this.email = response.data.email;
          this.name = response.data.name;
          this.fetchReview();
        })
        .catch((err) => {
          if(err.response.status == 400) {
            this.$router.push("/badRequest").catch(err => {
            });
          } else if(err.response.status == 500) {
            this.$router.push("/serverError").catch(err => {
            });
          }
        });
    },
    makeimgurl(imgurl) {
      var url = "../../../contents/"+imgurl;
      return url;
    },
    fetchReview() {
      axios
        .get(`${baseURL}/review/list/${this.pid}`)
        .then((response) => {
          this.slides = response.data;
          if (this.slides.length > 0) {
            this.flag = true;
          } else {
            this.flag = false;
          }
        })
        .catch((error) => {
          if(err.response.status == 400) {
            this.$router.push("/badRequest").catch(err => {
            });
          } else if(err.response.status == 500) {
            this.$router.push("/serverError").catch(err => {
            });
          }
        });
    },
    startDrag(e) {
      this.dragging = true;
      this.initialMouseX = e.pageX;
      this.initialCardsX = this.cardsX;
    },
    stopDrag() {
      this.dragging = false;
      const cardWidth = 290;
      const nearestSlide = -Math.round(this.cardsX / cardWidth);
      this.selectedIndex = Math.min(
        Math.max(0, nearestSlide),
        this.slides.length - 1
      );
      TweenLite.to(this, 0.3, { cardsX: -this.selectedIndex * cardWidth });
    },
    mouseMoving(e) {
      if (this.dragging) {
        const dragAmount = e.pageX - this.initialMouseX;
        const targetX = this.initialCardsX + dragAmount;
        this.cardsX = targetX;
      }
    },
    datecut(date) {
      var tempdatecut = date + "";
      return tempdatecut.substring(0, 10);
    },
    // reviewDelete(rvid) {
    //   this.$emit('review-delete',rvid)
    // },
    reviewDelete(rvid) {
      Swal.fire({
        width: 300,
        text: "후기를 삭제하시겠습니까?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#fff",
        cancelButtonColor: "#fff",
        confirmButtonText: '<a style="font-size:1rem; color:black">Delete</a>',
        cancelButtonText: '<a style="font-size:1rem; color:black">Cancel</a>',
      }).then((result) => {
        if (result.value) {
          const Toast = Swal.mixin({
            toast: true,
            position: "top-end",
            showConfirmButton: false,
            timer: 1000,
            timerProgressBar: true,
            onOpen: (toast) => {
              toast.addEventListener("mouseenter", Swal.stopTimer);
              toast.addEventListener("mouseleave", Swal.resumeTimer);
            },
          });

          Toast.fire({
            icon: "success",
            title: "후기 삭제 완료!",
          });
          axios
            .delete(`${baseURL}/review/delete/${rvid}`)
            .then(() => {
              this.fetchReview();
              // setTimeout(() => {
              //   this.$router.go();
              // }, 1000);
            })
            .catch((error) => {
              if(err.response.status == 400) {
                this.$router.push("/badRequest").catch(err => {
                });
              } else if(err.response.status == 500) {
                this.$router.push("/serverError").catch(err => {
                });
              }
            });
        }
      });
    },
    update(slide) {
      this.reviewInfo = slide;
      this.isUpdated = true;
    },
    reviewClose() {
      this.isUpdated = false;
    },
  },
  created() {
    if (this.$cookies.get("Auth-Token") != null) {
      this.authUser();
    }
    else {
      this.fetchReview();
    }
  },
};
</script>

<style>
.slider {
  overflow: hidden;
  background-color: white;
  width: 100%;
  height: 430px;
}
.slider-cards {
  cursor: pointer;
  position: relative;
  display: inline-flex;
  margin: 20px;
  z-index: 1;
}
.slider-card {
  display: inline-block;
  background-color: white;
  border: 0.1px solid gray;
  overflow: hidden;
  width: 260px;
  height: 360px;
  margin-right: 20px;
  border-radius: 12px;
  box-shadow: 40px 50px 20px -20px rgba(0, 0, 0, 0.3);
}
.review-img {
  width: 100%;
  height: 50%;
}
.profile-img {
  width: 50px;
  height: 47px;
  border-radius: 35px;
  font-size: 1rem;
  line-height: 1.33;
}
</style>