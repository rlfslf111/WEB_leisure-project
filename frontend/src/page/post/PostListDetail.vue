<template>
  <div class="container col-sm-12 col-md-12 col-lg-12 p-0">
    <!-- background image -->
    <div class="postdetail-img" style="display:block;">
      <div class="info-bg"></div>
    </div>

    <div class="container col-md-10" style="margin-top: 100px">
      <div class="column">
        <div class="card mt-5 mb-3" style="max-width: 100%;">
          <div class="row no-gutters">
            <div class="col-md-4">
              <img
                :src="makeimgurl(post.imgurl)"
                v-if="post.imgurl"
                class="card-img"
                style="height: 16rem; box-shadow:5px 5px 5px rgba(0,0,0,.15)"
                alt
              />
            </div>
            <div class="col-md-1"></div>
            <div class="col-md-7">
              <div class="card-body" style="padding: 0 0 0 20px">
                <div class="text">
                  <div class="d-flex justify-content-start">
                    <!-- hashTag -->
                    <small
                      class="text-primary mr-1 detailTags"
                      style="text-align:left; font-size:1rem; text-overflow:ellipsis; overflow: hidden; white-space: nowrap;"
                      v-for="hash in hashTag"
                      :key="hash.id"
                    >
                      <p @click="goTag(hash)" style="font-weight:bold;">#{{ hash }}</p>
                    </small>

                    <div class="ml-auto">
                      <!-- 카카오톡 공유하기 -->
                      <button
                        class="btn btn p-0"
                        @click="test()"
                        id="kakao-link-btn"
                        icon="share-fill"
                      >
                        <img
                          src="//developers.kakao.com/assets/img/about/logos/kakaolink/kakaolink_btn_small.png"
                          width="28px"
                        />
                      </button>
                    </div>
                  </div>
                  <div class="d-flex justify-content-start">
                    <!-- 업체 위치 -->
                    <p
                      class="card-text"
                      style="font-weight:bold;font-size: 1rem; color: rgb(168, 168, 168); text-align: left; text-overflow:ellipsis; overflow: hidden; white-space: nowrap;"
                    >[{{ post.location }}]</p>
                  </div>
                  <!-- 제목 -->
                  <p
                    class="card-text font-weight-bold"
                    style="font-weight:bold;font-size: 1.2rem; text-align: left;"
                  >[{{ post.activity }}]{{ post.title }}</p>
                  <!-- season, place check badge -->
                  <div
                    style="text-align: left; text-overflow:ellipsis; overflow: hidden; white-space: nowrap;"
                  >
                    <b-badge
                      v-if="this.springCheck == 1"
                      pill
                      variant
                      style="background-color: #F699CD;"
                    >Spring</b-badge>
                    <b-badge
                      v-if="this.summerCheck == 1"
                      pill
                      variant
                      style="background-color: #32a852;"
                    >Summer</b-badge>
                    <b-badge
                      v-if="this.autumnCheck == 1"
                      pill
                      variant
                      style="background-color: #CCA38D"
                    >Autumm</b-badge>
                    <b-badge
                      v-if="this.winterCheck == 1"
                      pill
                      variant
                      style="background-color: #D3D3D3"
                    >Winter</b-badge>
                    <b-badge
                      v-if="this.placeCheck == 'ground'"
                      pill
                      variant
                      style="background-color: #501B00"
                    >Ground</b-badge>
                    <b-badge
                      v-if="this.placeCheck == 'water'"
                      pill
                      variant
                      style="background-color: #003399"
                    >Water</b-badge>
                    <b-badge
                      v-if="this.placeCheck == 'sky'"
                      pill
                      variant
                      style="background-color: #8DCCE7"
                    >Sky</b-badge>
                  </div>
                  <!-- 사용 기간 -->
                  <div class="d-flex justify-content-between">
                    <p
                      class="card-text mt-3"
                      style="font-weight:bold;font-size: 1rem; text-overflow:ellipsis; overflow: hidden; white-space:nowrap;"
                    >유효기간 {{ post.sdate }}~{{ post.edate }}</p>
                  </div>
                  <!-- 이용 가격 -->
                  <div class="d-flex justify-content-between mt-3">
                    <!-- 신고하기 버튼 -->
                    <span
                      data-toggle="modal"
                      data-target="#indict"
                      class="mr-2 indict my-auto"
                      style="border:none; font-size:1.2rem"
                      title="신고하기"
                    >
                      <div class="d-flex" v-if="this.$cookies.get('Auth-Token')">
                        <i
                          class="fas fa-bell-slash my-auto"
                          style="font-size:0.8rem; color:crimson"
                        ></i>
                        <p class="my-auto" style="font-weight:bold;font-size:1rem; color:crimson">신고</p>
                      </div>
                      <!-- <i class="fas fa-bullhorn" style="color:red">신고</i> -->
                      <!-- <i class="fas fa-angry" style="color:red"></i> -->
                    </span>
                    <IndictPost :post="post" />
                    <p
                      class="card-text font-weight-bold mb-0"
                      style="font-weight:bold;font-size: 1.5rem; text-align: left; margin-bottom: 5px;
                  "
                    >{{ post.price }} 원</p>
                  </div>
                  <hr class="mt-0" />
                  <!-- like heart -->
                  <div class="d-flex justify-content-end mr-0 mt-3 mb-3">
                    <div class="d-flex justify-content-start">
                      <i
                        v-if="isheart"
                        class="fas fa-heart select-button mr-2"
                        style="text-align: right; font-size: 20px; color:crimson"
                        @click="registlike(post.pid)"
                      ></i>
                      <i
                        v-if="!isheart"
                        class="far fa-heart select-button mr-2"
                        style="text-align: right; font-size: 20px; color:crimson"
                        @click="registlike(post.pid)"
                      ></i>
                      <span style="font-weight:bold;">{{ post.likecnt }}명이 좋아요를 눌렀습니다.</span>
                    </div>
                  </div>
                  <!-- 장바구니, 구매 -->
                  <div class="d-flex justify-content-end" v-if="this.checkType == 'normal'">
                    <button
                      type="button"
                      class="btn btn-default mr-1"
                      @click="alertbasket(post)"
                      style="color:white; background-color:#86a5d4;font-weight:bold;"
                    >
                      <i class="fas fa-shopping-basket mr-2"></i>장바구니
                    </button>
                    <button
                      class="btn btn-danger"
                      @click="alertbuy(post)"
                      style="font-weight:bold;"
                    >
                      <i class="far fa-hand-point-up mr-2"></i>바로구매
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Scrollspy  -->
      <nav
        id="navbar-example2"
        class="navbar nav-info"
        style="position: sticky; top: 0; z-index:100;"
      >
        <ul class="nav justify-content-between" style="width:100%;">
          <li class="nav-item">
            <a
              class="nav-link info-link"
              href="#item"
              @click="scroll"
              style="font-size:0.9rem;"
            >상세정보</a>
          </li>
          <li class="nav-item">
            <a
              class="nav-link info-link"
              href="#corp"
              @click="scroll"
              style="font-size:0.9rem;"
            >업체정보</a>
          </li>
          <li class="nav-item">
            <a
              class="nav-link info-link"
              href="#review"
              @click="scroll"
              style="font-size:0.9rem;"
            >후기</a>
          </li>
          <li class="nav-item">
            <a class="nav-link info-link" href="#qna" @click="scroll" style="font-size:0.9rem;">Q&A</a>
          </li>
        </ul>
      </nav>
      <br />
      <div data-spy="scroll" data-target="#navbar-example2" data-offset="0">
        <!-- 상세 정봉 -->
        <h4 id="item" class="d-flex mb-3" style="font-weight:bold">상세정보</h4>
        <Viewer v-if="post.detail != null" :initialValue="post.detail" style="text-align:justify;" />
        <hr />
        <!-- 업체 정보 -->
        <h4 id="corp" class="d-flex mb-3" style="font-weight:bold">업체정보</h4>
        <p class="d-flex" style="white-space:pre-wrap; text-align:justify">{{ post.companyInfo }}</p>
        <hr />
        <!-- 지도 -->
        <p class="d-flex" style="font-size:1.5rem; font-weight:bold;">위치</p>
        <div id="map" style="max-width: 100%; height:300px;z-index:0"></div>
        <small class="d-flex mt-2" style="font-weight:bold; font-size:1rem;">{{ post.location }}</small>
        <hr class="mt-2" />
        <!-- 후기 -->
        <div class="d-flex justify-content-between mb-2">
          <div>
            <h4 id="review" class style="font-weight:bold">후기</h4>
          </div>
          <button
            data-toggle="modal"
            data-target="#reviewWrite"
            v-if="this.checkType == 'normal'"
            class="btn btn-sm"
            style="background-color:#86a5d4; color:white; height:1.8rem;font-weight:bold;"
          >
            <i class="fas fa-pen"></i>
            작성
          </button>
        </div>
        <ReviewWrite :pid="pid" :email="email" />
        <div class="d-none d-sm-block">
          <ReviewSlide :pid="pid" />
        </div>
        <div class="d-block d-sm-none d-md-none">
          <ReviewMobile :pid="pid" />
        </div>
        <hr />
        <!-- Q & A -->
        <h4 id="qna" class="d-flex mb-3" style="font-weight:bold">Q&A</h4>
        <!-- 댓글 List -->
        <div class="d-flex bg-white">
          총
          <span class="ml-1" style="color:#0047ab;">{{ receiveComment.length }}</span>개
        </div>
        <!-- 댓글 작성 -->

        <CommentInput class="mt-3" v-if="this.email" @create-comment="createcomment" />

        <!-- <div v-if="receiveComment.length ==  0" class="mt-2">
          <i class="far fa-surprise mr-1 mb-3"></i>등록된 질문이 없습니다. 처음으로 질문을 남겨보세요!
          <i class="far fa-surprise ml-1"></i>
        </div>-->
        <hr class="mb-0" />
        <CommentList
          v-for="comment in receiveComment"
          :key="comment.rid"
          :comment="comment"
          @comment-delete="commentDelete"
        />
      </div>

      <hr class="mt-0" />
      <!-- 글 수정 삭제 -->
      <div
        class="d-flex justify-content-end mt-3 mb-3"
        v-if="this.email == this.post.email | this.checkType == 'admin'"
      >
        <button
          class="btn btn-default mr-2"
          style="background-color:#86a5d4; color:white;font-weight:bold;"
          v-if="this.email == this.post.email"
          @click="goModify"
        >
          <i class="far fa-edit mr-2"></i>수정하기
        </button>
        <button class="btn btn-danger" @click="goDelete" style="font-weight:bold;">
          <i class="far fa-trash-alt mr-2"></i>삭제하기
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import "../../assets/css/postlistdetail.css";
import PostUpdateVue from "./PostUpdate.vue";

import CommentInput from "../../components/comment/CommentInput.vue";
import CommentList from "../../components/comment/CommentList.vue";

import ReviewSlide from "../../components/review/ReviewSlide.vue";
import ReviewWrite from "../../components/review/ReviewModal.vue";
import ReviewMobile from "../../components/review/ReviewMobile.vue";

import "codemirror/lib/codemirror.css";
import "@toast-ui/editor/dist/toastui-editor.css";
import { Viewer } from "@toast-ui/vue-editor";

import Swal from "sweetalert2";

import IndictPost from "../../components/modal/IndictPost.vue";

const baseURL = process.env.VUE_APP_BACKURL;

export default {
  components: {
    CommentInput,
    CommentList,
    ReviewSlide,
    ReviewWrite,
    ReviewMobile,
    IndictPost,
    Viewer,
  },
  data() {
    return {
      post: [],
      pid: "",
      email: "",
      receiveComment: [],
      hashTag: [],
      checkType: "",
      springCheck: "",
      summerCheck: "",
      autumnCheck: "",
      winterCheck: "",
      placeCheck: "",
      isheart : false,
    };
  },
  created() {
    this.pid = this.$route.params.ID;
    if (this.$cookies.get("Auth-Token") != null) {
      this.authUser();
    } else {
      this.getPost();
      this.fetchComment();
    }
    this.fetchHashTag();
  },
  methods: {
    goTag(tag) {
      this.$router.push({
        name: "TagList",
        params: { TAG: tag },
      });
    },
    scroll(evt) {
      evt.preventDefault();
      const href = evt.target.getAttribute("href");
      var location = document.querySelector(href).offsetTop;
      window.scrollTo({ top: location + 120, behavior: "smooth" });
    },
    authUser() {
      axios
        .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
        .then((response) => {
          this.checkType = response.data.checkType;
          this.email = response.data.email;
          this.pid = this.$route.params.ID;
          this.getPost();
          this.fetchComment();
          this.checklike();
        })
        .catch((err) => {
          if (err.response.status == 400) {
            this.$router.push("/badRequest").catch((err) => {});
          } else if (err.response.status == 500) {
            this.$router.push("/serverError").catch((err) => {});
          }
        });
    },
    checklike() {
      if (this.$cookies.get("Auth-Token") != null) {
        axios
          .get(`${baseURL}/like/checkpidlike/${this.email}/${this.pid}`)
          .then((res) => {
            if (res.data == 0) {
              this.isheart = false;
            } else if (res.data == 1) {
              this.isheart = true;
            }
          });
      }
    },
    registlike(pid) {
      if (this.email != "") {
        axios
          .get(`${baseURL}/like/registDelete/${this.email}/${pid}`)
          .then((res) => {
            // this.checklike();
            // this.reloading(this.page);
            if (res.data == "regist") {
              this.$toasted.show("좋아좋아요", {
                theme: "bubble",
                position: "top-right",
                duration: 1000,
              });
              this.isheart = true;
              this.getPost();
            } else {
              this.$toasted.show("좋아요 취소", {
                theme: "bubble",
                position: "top-right",
                duration: 1000,
              });
              this.isheart = false;
              this.getPost();
            }
          })
          .catch((err) => {
            alert(err);
          });
      } else {
        Swal.fire({
          icon: "error",
          text: "로그인 후 이용해주세요...",
          confirmButtonColor: "#fff",
          width: 350,
          confirmButtonText:
            '<a data-toggle="modal" data-target="#LoginModal" style="font-size:1rem; color:black" >Login</a>',
          showCancelButton: true,
          cancelButtonText: '<a style="font-size:1rem; color:black">Cancel</a>',
          cancelButtonColor: "#fff",
        }).then((result) => {
          Swal.close();
        });
      }
    },
    makeimgurl(imgurl) {
      var url = "../../../contents/" + imgurl;
      return url;
    },
    test() {
      Kakao.Link.sendDefault({
        // container: "#kakao-link-btn",
        objectType: "feed",
        content: {
          title: this.post.title, // 콘텐츠의 타이틀
          description: this.post.activity, // 콘텐츠 상세설명
          imageUrl: document.images[2].src, // 썸네일 이미지
          link: {
            webUrl: "http://i3b206.p.ssafy.io:3000/#/posts/" + this.pid,
            mobileWebUrl: "http://i3b206.p.ssafy.io:3000/#/posts/" + this.pid,
          },
        },
        social: {
          likeCount: this.post.likecnt, // LIKE 개수
          commentCount: this.receiveComment.length, // 댓글 개수
          // sharedCount: 845,
        },
        buttons: [
          {
            title: "Open!", // 버튼 제목
            link: {
              mobileWebUrl: "http://i3b206.p.ssafy.io:3000/#/posts/" + this.pid,
              webUrl: "http://i3b206.p.ssafy.io:3000/#/posts/" + this.pid,
            },
          },
        ],
      });
    },
    // goinfo() {
    //   this.$router.go();
    // },
    getPost() {
      axios
        .get(`${baseURL}/post/detail/${this.$route.params.ID}`)
        .then((res) => {
          this.springCheck = res.data.spring;
          this.summerCheck = res.data.summer;
          this.autumnCheck = res.data.autumn;
          this.winterCheck = res.data.winter;
          this.placeCheck = res.data.place;
          this.post = res.data;
          this.mapView(this.post.location);
        })
        .catch((err) => {
          if (err.response.status == 400) {
            this.$router.push("/badRequest").catch((err) => {});
          } else if (err.response.status == 500) {
            this.$router.push("/serverError").catch((err) => {});
          }
        });
    },
    goModify() {
      this.$router.push({
        name: "PostUpdate",
        params: { ID: this.pid },
      });
    },
    goDelete() {
      Swal.fire({
        width: 350,
        text: "삭제하시겠습니까?",
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
            title: "글이 삭제되었습니다.",
          });
          axios
            .delete(`${baseURL}/post/delete/${this.$route.params.ID}`)
            .then(() => {
              this.$router.push(`/posts`);
            })
            .catch((error) => {
              if (err.response.status == 400) {
                this.$router.push("/badRequest").catch((err) => {});
              } else if (err.response.status == 500) {
                this.$router.push("/serverError").catch((err) => {});
              }
            });
        }
      });
    },
    createcomment(commentData) {
      const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 1500,
        timerProgressBar: true,
        onOpen: (toast) => {
          toast.addEventListener("mouseenter", Swal.stopTimer);
          toast.addEventListener("mouseleave", Swal.resumeTimer);
        },
      });
      axios
        .post(`${baseURL}/reply/register`, commentData)
        .then((response) => {
          commentData.content = "";
          this.fetchComment();
          Toast.fire({
            icon: "success",
            title: "댓글 작성 완료!",
          });
        })
        .catch((error) => {
          if (err.response.status == 400) {
            this.$router.push("/badRequest").catch((err) => {});
          } else if (err.response.status == 500) {
            this.$router.push("/serverError").catch((err) => {});
          }
        });
    },
    fetchComment() {
      axios
        .get(`${baseURL}/reply/list/${this.$route.params.ID}`)
        .then((response) => {
          this.receiveComment = response.data;
        })
        .catch((error) => {
          if (err.response.status == 400) {
            this.$router.push("/badRequest").catch((err) => {});
          } else if (err.response.status == 500) {
            this.$router.push("/serverError").catch((err) => {});
          }
        });
    },
    commentDelete(comment) {
      Swal.fire({
        width: 350,
        text: "댓글을 삭제하시겠습니까?",
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
            title: "댓글이 삭제되었습니다.",
          });
          axios
            .delete(`${baseURL}/reply/delete/${comment.rid}`)
            .then((response) => {
              this.fetchComment();
            })
            .catch((error) => {
              if (err.response.status == 400) {
                this.$router.push("/badRequest").catch((err) => {});
              } else if (err.response.status == 500) {
                this.$router.push("/serverError").catch((err) => {});
              }
            });
        }
      });
    },
    alertbasket(post) {
      let isin = 0;
      axios
        .get(`${baseURL}/cart/check/${this.email}/${this.pid}`)
        .then((res) => {
          if (res.data) {
            const Toast = Swal.mixin({
          toast: true,
          position: 'top-end',
          showConfirmButton: false,
          timer: 2000,
          timerProgressBar: true,
          onOpen: (toast) => {
            toast.addEventListener('mouseenter', Swal.stopTimer)
            toast.addEventListener('mouseleave', Swal.resumeTimer)
          }
        })

        Toast.fire({
          icon: 'error',
          title: '동일한 상품이 장바구니에 담겨있습니다!'
        })
          } else {
            Swal.fire({
              title: `${post.title}`,
              text: "장바구니에 담겼습니다.",
              imageUrl: `${this.makeimgurl(post.imgurl)}`,
              imageWidth: 400,
              imageHeight: 200,
              imageAlt: "Custom image",
            }),
              axios
                .get(`${baseURL}/cart/regist/${this.email}/${this.pid}`)
                .then((res) => {
                  this.posts = this.res;
                })
                .catch((err) => {
                  if (err.response.status == 400) {
                    this.$router.push("/badRequest").catch((err) => {});
                  } else if (err.response.status == 500) {
                    this.$router.push("/serverError").catch((err) => {});
                  }
                });
          }
        })
        .catch((err) => {
          console.log(err);
        });
      //장바구니에 있는지 확인

      // alert(`'${title}'상품을 장바구니에 담았습니다!`)
    },
    alertbuy(post) {
      Swal.fire({
        width: 350,
        text: "단일상품은 구매 할인적용 불가합니다. 계속하시겠습니까?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#fff",
        cancelButtonColor: "#fff",
        confirmButtonText:
          '<a style="font-size:1rem; color:black">구매하기</a>',
        cancelButtonText: '<a style="font-size:1rem; color:black">취소하기</a>',
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
          let th = this;

          var IMP = window.IMP; // 생략가능
          var msg;
          IMP.init("imp40062977");

          IMP.request_pay(
            {
              pg: "html5_inicis",
              pay_method: "card",
              merchant_uid: "merchant_" + new Date().getTime(),
              name: "링키비티",
              amount: this.post.price,
              buyer_email: "iamport@siot.do",
              buyer_name: "구매자이름",
              buyer_tel: "010-1234-5678",
              buyer_addr: "서울특별시 강남구 삼성동",
              buyer_postcode: "123-456",
            },
            function (rsp) {
              if (rsp.success) {
                var msg = "결제가 완료되었습니다.";
                msg += "고유ID : " + rsp.imp_uid;
                msg += "상점 거래ID : " + rsp.merchant_uid;
                msg += "결제 금액 : " + rsp.paid_amount;
                msg += "카드 승인번호 : " + rsp.apply_num;

                axios
                  .get(
                    `${baseURL}/purchase/registOne/${th.pid}/${th.email}/${th.post.price}`
                  )
                  .then((response) => {
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
                      title: `${rsp.paid_amount}원 결제 완료!`,
                    });
                    setTimeout(() => {
                      th.$router.go();
                    }, 1000);
                  })
                  .catch((err) => {
                    console.log(err);
                  });
              } else {
                var msg = "결제에 실패하였습니다.";
                msg += "에러내용 : " + rsp.error_msg;
                const Toast = Swal.mixin({
                  toast: true,
                  position: 'top-end',
                  showConfirmButton: false,
                  timer: 2000,
                  timerProgressBar: true,
                  onOpen: (toast) => {
                    toast.addEventListener('mouseenter', Swal.stopTimer)
                    toast.addEventListener('mouseleave', Swal.resumeTimer)
                  }
                })

                Toast.fire({
                  icon: 'error',
                  title: `${msg}`
                })
              }
              // alert(msg);
            }
          );
        }
      });
    },
    mapView(loc) {
      var mapContainer = document.getElementById("map"), // 지도를 표시할 div
        mapOption = {
          center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
          level: 3, // 지도의 확대 레벨
        };

      // 지도를 생성합니다
      var map = new kakao.maps.Map(mapContainer, mapOption);

      // 주소-좌표 변환 객체를 생성합니다
      var geocoder = new kakao.maps.services.Geocoder();

      // 주소로 좌표를 검색합니다
      geocoder.addressSearch(loc, function (result, status) {
        // 정상적으로 검색이 완료됐으면
        if (status === kakao.maps.services.Status.OK) {
          var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

          // 결과값으로 받은 위치를 마커로 표시합니다
          var marker = new kakao.maps.Marker({
            map: map,
            position: coords,
          });
          // var test = '<div class="row" style="height:50px;text-align:center;"><div class="col-md-12">'+loc+'</div></div>';

          // 인포윈도우로 장소에 대한 설명을 표시합니다
          var infowindow = new kakao.maps.InfoWindow({
            // content: test,
          });
          // infowindow.open(map, marker);

          // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
          map.setCenter(coords);
        }
      });
    },
    fetchHashTag() {
      axios
        .get(`${baseURL}/tag/list/${this.pid}`)
        .then((response) => {
          this.hashTag = response.data;
        })
        .catch((error) => {
          if (err.response.status == 400) {
            this.$router.push("/badRequest").catch((err) => {});
          } else if (err.response.status == 500) {
            this.$router.push("/serverError").catch((err) => {});
          }
        });
    },
  },
};
</script>

<style>
.indict {
  cursor: pointer;
}
.detailTags :hover {
  text-decoration: underline;
  cursor: pointer;
}
</style>
