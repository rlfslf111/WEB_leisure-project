<template>
  <div>
    <div v-if="!flag">
      <i class="far fa-surprise mr-1 mb-3"></i>등록된 후기가 없습니다. 처음으로 후기를 남겨보세요!
      <i class="far fa-surprise ml-1"></i>
    </div>
    <div v-if="flag">
      <b-carousel
        id="carousel-1"
        :interval="4000"
        controls
        indicators
        img-width="1024"
        img-height="480"
        style="text-shadow: 1px 1px 2px #333;"
      >
        <!-- Text slides with image -->
        <b-carousel-slide v-for="slide in slides" :key="slide.rvid">
          <template v-slot:img>
          <img
            class="d-block img-fluid w-100"
            width="1024"
            height="480"
            v-if="slide.img"
            :src="makeimgurl(slide.img)"
            alt="image slot"
          >
          <img
            class="d-block img-fluid w-100"
            width="1024"
            height="480"
            v-if="!slide.img"
            src="../../assets/img/noimage.jpg"
            alt="image slot"
          >
        </template>
          <div class="d-flex justify-content-center align-items-center" style="height:15rem;">
            <div class="mb-5" style="background-color:rgba( 0, 0, 0, 0.4 );">
            <!-- background-color:black; -->
            <div class="d-flex justify-content-center" style="width:100%">
              <!-- 프로필 보여주기 -->
              <img class="profile-image d-flex my-2 ml-3 mr-4 " style="width:15%; height:3rem !important;" v-if="slide.proimg" :src="makeimgurl(slide.proimg)"/>
              <img class="profile-image d-flex my-2 ml-3 mr-4 " style="width:15%; height:3rem !important;" v-if="!slide.proimg" src="../../assets/img/noimage.jpg"/>
              <div class="mt-2 mr-4 ml-0">
                <div class="d-flex">
                  <small
                    class="d-flex align-items-center mb-2"
                    style="font-weight:bold"
                  >{{slide.nickname}}</small>
                  <br />
                </div>
                <div class="d-flex">
                  <i
                    class="fas fa-star d-flex"
                    style="color:Salmon;"
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
                <div class="d-flex align-items-end mt-1 mr-3">
                  <small style="font-weight:bold">{{datecut(slide.createDate)}}</small>
                </div>
              </div>
              <div v-if="email != slide.email" class="d-flex align-items-center mt-4 mr-3">
                <small style="font-weight:bold">{{datecut(slide.createDate)}}</small>
              </div>
            </div>
            <div class="d-flex row" style="padding-left:5%">
              <!-- 제목 -->
              <div class="d-flex mt-3 col-12">
                <span style="font-weight:bold; font-size:1.2rem; text-align:justify; font-family: 'Jua', sans-serif;">[{{slide.title}}]</span>
              </div>
              <!-- 내용 -->
              <div class="d-flex mt-3 col-12 mb-2">
                <small style="text-align:justify" class="mr-2">{{slide.content}}</small>
              </div>
            </div>
            </div>
          </div>
        </b-carousel-slide>
      </b-carousel>
      <ReviewUpdate v-if="this.isUpdated" :reviewInfo="reviewInfo" @review-close="reviewClose" />
    </div>
  </div>
</template>

<script>
import axios from "axios";
const baseURL = process.env.VUE_APP_BACKURL;

import ReviewUpdate from "./ReviewUpdateModal";

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
          this.fetchReview();
        })
        .catch((err) => {
          console.log(err.response);
        });
    },
    fetchReview() {
      axios
        .get(`${baseURL}/review/list/${this.pid}`)
        .then((response) => {
          this.slides = response.data;
          if (this.slides.length > 0) {
            this.flag = true;
            // for(var i=0;i<this.slides.length;i++){
            //     if(!this.slides[i].img){
            //         this.slides[i].img = "https://picsum.photos/1024/480/?image=54";
            //     }
            // }
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
    datecut(date) {
      var tempdatecut = date + "";
      return tempdatecut.substring(0, 10);
    },
    makeimgurl(imgurl) {
      var url = "../../../contents/"+imgurl;
      return url;
    },
    update(slide) {
      this.reviewInfo = slide;
      this.isUpdated = true;
    },
    reviewClose() {
      this.isUpdated = false;
    },
    //   reviewDelete(rvid) {
    //     this.$emit('review-delete',rvid)
    //   },
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
              //     this.$router.go();
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
  },
  created() {
    if (this.$cookies.get("Auth-Token") != null) {
      this.authUser();
    } else {
      this.fetchReview();
    }
  },
};
</script>

<style>
.profile-image {
  width: 25%;
  height: 25% !important;
  border-radius: 15px;
  /* font-size: 5rem; */
  /* line-height: 51; */
}
</style>