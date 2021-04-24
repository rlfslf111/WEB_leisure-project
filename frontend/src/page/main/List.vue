<template>
  <div>
    <div class="container col-sm-12 col-md-12 col-lg-12 p-0">
      <!-- background image -->
      <div class="main-img" style="display:block; height: 45rem;">
        <div class="bg"></div>

        <div>
          <div style="height:20rem"></div>
          <span class="main-title">원하는 국내 액티비티를 패키지로 즐기자!</span>
          <div class="col-md-12 mt-5">
            <div class="row">
              <div class="col-md-3"></div>
              <div class="col-md-6">
                <div class="input-group-append">
                  <b-form-tags
                    v-model="tName"
                    tag-variant="primary"
                    separator=" "
                    remove-on-delete
                    placeholder="액티비티를 해쉬태그로 검색해보세요!!"
                    class="container tagText"
                  ></b-form-tags>
                  <!-- <div class="col-sm-1 bg-white"></div> -->
                  <div class="d-flex justify-content-end bg-white align-items-center pr-3 sicon">
                    <i
                      class="fas fa-2x fa-search searchIcon"
                      style="cursor:pointer;"
                      @click="tagSearch(tName)"
                    ></i>
                  </div>
                </div>
              </div>
              <div class="col-md-3"></div>
            </div>
          </div>
        </div>
      </div>

      <!-- 태그 검색 결과 Div -->
      <div v-if="this.tagFlag == true">
        <div class="post mt-5">
          <div class="container resultTitle">
            <div v-if="this.pids.length > 0">
              [
              <span v-for="(t, index) in this.resultTitle" :key="index">#{{ t }}</span>
              ]의 액티비티 :)
            </div>
            <div v-if="this.pids.length == 0">
              [
              <span v-for="(t, index) in this.resultTitle" :key="index">#{{ t }}</span>
              ]을(를) 찾지 못했어요 :(
            </div>
          </div>

          <div class="container col-md-8">
            <div class="row justify-content-left" v-if="tPosts.length > 0">
              <div
                class="col-12 col-sm-12 col-md-3 card-deck"
                style="margin:auto 0; padding:0 20px ;"
                v-for="(post, index) in tPosts"
                :key="index"
              >
                <div class="card mb-3 profile-post mr-0 ml-0">
                  <div class="card-body" style="padding: 0;">
                    <div class="box" @click="getdetail(post.pid)">
                      <img
                        :src="makeimgurl(post.imgurl)"
                        v-if="post.imgurl"
                        class="card-img postlist-img"
                        style="height:10rem; box-shadow:5px 5px 5px rgba(0,0,0,.15)"
                      />
                      <div
                        class="card-img-overlay pt-0 pr-2"
                        style="text-align:right; font-size:0.7rem; font-weight:400; color: white; widht:1rem; height:1rem; "
                      >
                        <span
                          class="pr-2 pl-2 pb-1"
                          style="background-color:rgba(0,0,0,0.3);z-index:34;"
                        >
                          <i class="fa fa-map-marker" style="font-size:0.7rem;"></i>
                          {{ localarea(post.location) }}
                        </span>
                      </div>
                    </div>
                    <div class="col-md-12 p-0">
                      <div class="card-body" style="padding: 5px; height:10rem;">
                        <div
                          v-for="tagg in tag"
                          :key="tagg.pid"
                          style="text-align: left; text-overflow:ellipsis;overflow: hidden;white-space: nowrap;"
                        >
                          <div v-if="tagg.pid == post.pid">
                            <span
                              class="card-text mb-2 text-primary"
                              v-for="tagname in tagg.tag"
                              :key="tagname"
                              style="font-size: 0.8rem; font-weight:bold;"
                            >#{{ tagname }}</span>
                          </div>
                        </div>
                        <h5
                          class="card-title-post m-0"
                          @click="getdetail(post.pid)"
                          style="font-size: 1rem; text-align: left; text-overflow:ellipsis;overflow: hidden;white-space: nowrap;"
                        >{{ post.title }}</h5>
                        <div class="card-text mb-2" style="text-align: left; font-size: 0.8rem;">
                          <span>
                            <i class="fas fa-star mr-1" style="color:Salmon; font-size:0.7rem;"></i>
                            {{ round(post.star) }}
                          </span>
                        </div>

                        <div class="text d-flex justify-content-between">
                          <p
                            class="card-text"
                            style="font-size: 1rem; font-weight:bold; text-align: left; text-overflow:ellipsis;overflow: hidden;white-space: nowrap;"
                          >{{ addComma(post.price) }}원</p>
                          <!-- heart like -->
                          <div id="heart" @click="registlike(post.pid)">
                            {{ post.likecnt }}
                            <i
                              v-if="check(post.pid)"
                              class="fas fa-heart select-button like-button"
                              style="text-align: right; font-size: 20px; color:crimson;"
                            ></i>
                            <i
                              v-if="!check(post.pid)"
                              class="far fa-heart"
                              style="text-align: right; font-size: 20px;"
                            ></i>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- infinite loading -->
            <infinite-loading
              :identifier="infiniteId"
              @infinite="infiniteHandler"
              spinner="waveDots"
            >
              <div slot="no-results"></div>
            </infinite-loading>
          </div>
        </div>
      </div>

      <!-- 일반 메인리스트 -->
      <div class="col-md-11" style="margin: 3rem auto;" v-if="this.tagFlag == false">
        <!-- main3button -->
        <div class="col-12 container row mb-5 m-0 p-0">
          <div
            class="col-sm-12 col-md-3 main-btn-intro mt-1 mb-1 d-flex justify-content-center align-items-center"
            style="cursor:pointer; border:1px solid black; border-radius:10px; height:5rem; border:none; box-shadow: 0 0 8px rgba(0, 0, 0, .15); font-size:1.2rem;"
            data-toggle="modal"
            data-target="#surveymodal"
          >
            <i class="far fa-handshake mr-2"></i>
            <span style="font-weight:bold">액티비티 추천</span>
          </div>
          <SurveyModal />
          <div class="col-sm-0 col-md-1" style="margin-right:2.1%; margin-left:2.1%;"></div>
          <div
            class="col-sm-12 col-md-3 main-btn-item mt-1 mb-1 d-flex justify-content-center align-items-center"
            style="cursor:pointer; border:1px solid black; border-radius:10px; height:5rem; border:none; box-shadow: 0 0 8px rgba(0, 0, 0, .15); font-size:1.2rem;"
            @click="goPost"
          >
            <i class="fas fa-gift mr-2"></i>
            <span style="font-weight:bold">상품 둘러보기</span>
          </div>
          <div class="col-sm-0 col-md-1" style="margin-right:2%; margin-left:2.1%;"></div>
          <div
            class="col-sm-12 col-md-3 main-btn-notice mt-1 mb-1 d-flex justify-content-center align-items-center"
            style="cursor:pointer; border:1px solid black; border-radius:10px; height:5rem; border:none; box-shadow: 0 0 8px rgba(0, 0, 0, .15); font-size:1.2rem;"
            @click="goNews"
          >
            <i class="far fa-newspaper mr-2"></i>
            <span style="font-weight:bold">액티비티 소식</span>
          </div>
        </div>

        <!-- carousel -->
        <div
          id="carouselExampleControls"
          class="carousel slide"
          style="margin: auto;"
          data-ride="carousel"
        >
          <ul class="carousel-indicators">
            <li data-target="#carouselExampleControls" data-slide-to="0" class="active"></li>
            <li data-target="#carouselExampleControls" data-slide-to="1"></li>
            <li data-target="#carouselExampleControls" data-slide-to="2"></li>
            <li data-target="#carouselExampleControls" data-slide-to="3"></li>
          </ul>
          <div class="carousel-inner" style="height:18rem">
            <div class="carousel-item active">
              <img
                src="../../assets/img/emotion.jpg"
                class="d-block w-100"
                style="height:18rem;filter:brightness(80%);"
                alt="paragliding"
              />
              <div class="carousel-caption" style="text-align:right; width:80%; height:90%; ">
                <h1 style="font-weight:bold; font-size:50px">액티비티는 역시</h1>
                <h1 style="font-weight:bold; font-size:50px">링키비티</h1>
                <p style="font-weight:bold;">"링키비티 가입 후 집 나간 여자친구가 안돌아와요"</p>
              </div>
            </div>
            <div class="carousel-item">
              <img
                src="../../assets/img/mountain.jpg"
                class="d-block w-100"
                style="height:18rem;filter:brightness(80%);"
                alt="snowboarding"
              />
              <div class="carousel-caption" style="text-align:right; width:80%; height:90%; ">
                <h1 style="font-weight:bold; font-size:50px">링키비티와 함께</h1>
                <h1 style="font-weight:bold; font-size:50px">매년 꿀잼 라이프 시작!</h1>
                <p style="font-weight:bold;">액티비티 입문자는 추천서비스를 이용해보세요!</p>
              </div>
            </div>
            <div class="carousel-item">
              <img
                src="../../assets/img/sailing.jpg"
                class="d-block w-100"
                style="height:18rem;filter:brightness(80%);"
                alt="wingsuit"
              />
              <div class="carousel-caption" style="text-align:right; width:80%; height:90%; ">
                <h1 style="font-weight:bold; font-size:50px;">한번의 예약으로</h1>
                <h1 style="font-weight:bold; font-size:50px;">최대 15%까지 할인!</h1>
                <p style="font-weight:bold;">원하는 상품 패키징으로 저렴하게 즐기세요!</p>
              </div>
            </div>
            <div class="carousel-item">
              <img
                src="../../assets/img/snow.jpg"
                class="d-block w-100"
                style="height:18rem;filter:brightness(80%);"
                alt="wingsuit"
              />
              <div class="carousel-caption" style="text-align:right; width:80%; height:90%; ">
                <h1 style="font-weight:bold; font-size:50px">봄 여름 가을 겨울</h1>
                <h1 style="font-weight:bold; font-size:50px">구애받지 말고!</h1>
                <p style="font-weight:bold;">사계절 상관없이 언제든지 예약 가능!</p>
              </div>
            </div>
          </div>
          <!-- <a
            class="carousel-control-prev"
            href="#carouselExampleControls"
            role="button"
            data-slide="prev"
            style="opacity: 0;"
          >
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
          </a>
          <a
            class="carousel-control-next"
            href="#carouselExampleControls"
            role="button"
            data-slide="next"
            style="opacity: 0;"
          >
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
          </a>-->
        </div>

        <!-- HOt item -->
        <div class="d-flex justify-content-between">
          <p class="hot-item mb-0" @click="goPost">HOT ITEM</p>
          <span class="more" @click="goPost" style="font-weight:bold;">+ more</span>
        </div>

        <div class="input-group-pretend row">
          <div
            class="card p-3 col-12 col-sm-6 col-md-3"
            v-for="(post, index) in posts"
            :key="index"
            style="width: 17rem; border: none;"
          >
            <img
              :src="makeimgurl(post.imgurl)"
              v-if="post.imgurl"
              class="card-img-top postlist-img"
              style="height:12rem; cursor: pointer; box-shadow:5px 5px 5px rgba(0,0,0,.15);"
              @click="getdetail(post.pid)"
            />
            <div class="card-body p-0">
              <p
                class="card-text mt-2 mb-0"
                style="text-overflow:ellipsis;overflow: hidden;white-space: nowrap; font-weight: bold; color: black; text-align:left;"
              >{{ post.title }}</p>
              <p
                class="card-text d-flex justify-content-start"
                style="text-overflow:ellipsis; overflow: hidden; white-space: nowrap;"
              >
                <i
                  class="fas fa-heart select-button like-button mr-2 mt-1"
                  style="text-align: left; font-size: 18px; color: crimson; "
                ></i>
                {{ post.likecnt }}
              </p>
            </div>
          </div>
        </div>

        <!-- RECENT REVIEW -->
        <div class="d-flex justify-content-between">
          <p class="recent-review mb-0">RECENT REVIEW</p>
          <!-- <span class="more">+ more</span> -->
        </div>
        <div class="row" v-if="reviews.length > 0">
          <div v-for="(slide, index) in reviews" :key="index" class="col-6 col-sm-4 col-md-2 p-3">
            <!-- <div class="card mb-3 profile-post mr-0 ml-0"> -->
            <div
              class="card-body list-profile"
              style="padding: 0; cursor: pointer;"
              @click="getdetail(slide.pid)"
            >
              <!-- img 보여주기 -->
              <img
                v-if="slide.img"
                class="review-img"
                :src="makeimgurl(slide.img)"
                style="height:8rem; box-shadow:5px 5px 5px rgba(0,0,0,.15);border-top-right-radius:5px;border-top-left-radius:5px;"
              />
              <img
                v-if="!slide.img"
                class="review-img"
                src="../../assets/img/noimage.jpg"
                style="height:8rem; box-shadow:5px 5px 5px rgba(0,0,0,.15);"
              />
              <!-- 프로필 보여주기 -->
              <div class="d-flex justify-content-between">
                <img
                  class="user-img d-flex m-2"
                  :src="makeimgurl(slide.proimg)"
                  v-if="slide.proimg"
                  style="border-radius:70px; width:50px; height:50px;"
                />
                <div class="mt-2 mr-4">
                  <div class="d-flex">
                    <small
                      class="d-flex align-items-center"
                      style="font-weight:bold"
                    >{{ slide.nickname }}</small>
                    <br />
                  </div>
                  <div class="d-flex">
                    <i
                      class="fas fa-star"
                      style="color:Salmon; font-size:0.7rem;"
                      v-for="i in slide.star"
                      :key="i.id"
                    ></i>
                  </div>
                  <div class="d-flex align-items-end mt-1">
                    <small style="font-weight:bold; white-space:nowrap">
                      {{
                      datecut(slide.createDate)
                      }}
                    </small>
                  </div>
                </div>
              </div>
              <!-- 제목 -->
              <div class="d-flex p-2" style="justify-content:start; ">
                <span style="font-weight:bold; text-align:justify">{{ slide.title }}</span>
              </div>
            </div>
            <!-- </div> -->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import "../../assets/css/list.css";
import Footer from "../../components/common/Footer.vue";
import SurveyModal from "../../components/modal/SurveyModal.vue";
import Swal from "sweetalert2";
import InfiniteLoading from "vue-infinite-loading";

const baseURL = process.env.VUE_APP_BACKURL;

export default {
  name: "Post",
  components: { Footer, SurveyModal, InfiniteLoading },
  created() {
    if (this.$route.params.TAG != null) {
      this.oneTag = this.$route.params.TAG;
      this.tName[0] = this.oneTag;
      this.tagSearch(this.tName);
    }
    this.init();
    this.reviewinit();
  },
  watch: {},

  methods: {
    init() {
      axios
        .get(`${baseURL}/post/listbylike/`)
        .then((res) => {
          this.posts = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    reviewinit() {
      axios
        .get(`${baseURL}/review/getLastReview`)
        .then((res) => {
          this.reviews = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    makeimgurl(imgurl) {
        var url = "../../../contents/" + imgurl;
        return url;
    },
    goNews: function () {
      this.$router.push("/news/");
    },
    goPost: function () {
      this.$router.push("/posts/");
      this.$router.go();
    },
    getdetail(pid) {
      scroll(0, 0);
      this.$router.push({
        name: "PostListDetail",
        params: { ID: pid },
      });
    },
    datecut(date) {
      var tempdatecut = date + "";
      return tempdatecut.substring(0, 10);
    },
    round(star) {
      return Math.round(star * 10) / 10.0;
    },
    infiniteHandler($state) {
      if (this.pids.length == 0) {
        $state.complete();
      } else {
        axios
          .get(`${baseURL}/post/getTagList/${this.pids}/${this.page}`)
          .then((res) => {
            setTimeout(() => {
              if (res.data.length) {
                this.tPosts = this.tPosts.concat(res.data);
                $state.loaded();
                this.page += 1;
                if (this.tPosts.length / 8 < 1) {
                  $state.complete();
                }
                this.nextTag();
              } else {
                $state.complete();
              }
            }, 100);
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
    getdetail(pid) {
      scroll(0, 0);
      this.$router.push({
        name: "PostListDetail",
        params: { ID: pid },
      });
    },
    registlike(pid) {
      if (this.email != "") {
        axios
          .get(`${baseURL}/like/registDelete/${this.email}/${pid}`)
          .then((res) => {
            this.checklike();
            this.reloading(this.page);
            if (this.check(pid) == false) {
              this.$toasted.show("좋아좋아요", {
                theme: "bubble",
                position: "top-right",
                duration: 1000,
              });
            } else {
              this.$toasted.show("좋아요 취소", {
                theme: "bubble",
                position: "top-right",
                duration: 1000,
              });
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
    check(pid) {
      for (var i = 0; i < this.postLike.length; i++) {
        if (this.postLike[i] == pid) {
          return true;
        }
      }
      return false;
    },
    reloading(pg) {
      axios
        .get(`${baseURL}/post/getTagReloading/${this.pids}/${pg - 1}`)
        .then((res) => {
          this.tPosts = res.data;
          this.nextTag();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    checklike() {
      axios
        .get(`${baseURL}/like/check/${this.email}`)
        .then((res) => {
          this.postLike = res.data;
        })
        .catch((err) => {
          alert(err);
        });
    },
    nextTag() {
      this.tag = [];
      for (let i = 0; i < this.tPosts.length; i++) {
        axios
          .get(`${baseURL}/tag/list/${this.tPosts[i].pid}`)
          .then((res) => {
            let a = { tag: res.data, pid: this.tPosts[i].pid };
            this.tag.push(a);
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
    localarea(location) {
      var la = location + "";
      return la.substring(0, 2);
    },
    addComma(num) {
      var regexp = /\B(?=(\d{3})+(?!\d))/g;
      return num.toString().replace(regexp, ",");
    },
    userCheck() {
      if (this.$cookies.get("Auth-Token") != null) {
        axios
          .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
          .then((response) => {
            this.email = response.data.email;
            this.checklike();
          })
          .catch((err) => {
            console.log(err.response);
          });
      }
    },
    tagSearch(tags) {
      this.userCheck();
      this.infiniteId += 1;
      if (this.tName.length == 0) {
        // alert("해쉬태그를 입력해주세요");
        this.$router.go();
      } else {
        this.resultTitle = tags;
        this.page = 0;
        axios
          .get(`${baseURL}/tag/search/` + tags)
          .then((res) => {
            this.pids = res.data;
            this.tPosts = [];
            this.tagFlag = true;
            scroll(0, 350);
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
  },
  data: () => {
    return {
      page: 0,
      infiniteId: 0,
      tPosts: [],
      tName: [],
      postLike: [],
      resultTitle: [],
      oneTag: "",
      tag: [],
      tags: [],
      email: "",
      password: "",
      itemcount: 0,
      pids: [],
      tagFlag: false,
      posts: {
        pid: "",
        email: "",
        activity: "",
        title: "",
        location: "",
        imgurl: "",
        price: "",
        sdate: "",
        edate: "",
        likecnt: "",
      },
      reviews: {},
    };
  },
};
</script>

<style scoped>
.tagText {
  font-size: 1.2rem !important;
  text-align: center !important;
  vertical-align: middle !important;
  width: 90%;
  margin: 0px !important;
  height: auto !important;
  border: none !important;
  border-top-left-radius: 10px !important;
  border-bottom-left-radius: 10px !important;
  border-top-right-radius: 0px !important;
  border-bottom-right-radius: 0px !important;
  box-shadow: 1px 1px 10px 0 rgba(22, 22, 22, 0.61) !important;
  padding: 20px !important;
}
.sicon {
  border-top-right-radius: 10px !important;
  border-bottom-right-radius: 10px !important;
  width: 10% !important;
}
.searchIcon {
  /* color: rgb(69, 123, 221); */
  color: #86a5d4;
  /* padding-top: 15px; */
}
.searchIcon:hover {
  color: #0047ab;
}
.main-title {
  font-size: 3.3rem;
  position: relative;
  /* z-index: 1000; */
  font-weight: bold;
  opacity: 0.9;
}
input:focus::-webkit-input-placeholder,
textarea:focus::-webkit-input-placeholder {
  color: transparent;
}
.resultTitle {
  margin-bottom: 50px;
  font-weight: bold;
  font-size: 2rem;
}
</style>
