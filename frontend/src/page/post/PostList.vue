<template>
  <div class="container col-sm-12 col-md-12 col-lg-12 p-0">
    <!-- background image -->
    <div class="post-img" style="display:block;">
      <div class="basket-img-bg"></div>
      <div class="postment d-flex ml-5">액티비티</div>
    <p class="postmentp d-flex ml-5">다양한 액티비티를 한눈에!</p></div>

    <div class="post mt-5">
      <div class="container col-md-11">
        <div class="input-group mb-5 col-md-9 mx-auto">
          <div class="input-group-prepend">
            <select
              class="btn dropdown-toggle text-black"
              style="height:50px; border: 1px solid #86a5d4; z-index: 1; background-color:#86a5d4; color:white; font-weight:bold;"
              aria-haspopup="true"
              aria-expanded="false"
              v-model="key"
            >
              <div role="separator" class="dropdown-divider" ></div>
              <option value disabled class = "selectlist">검색조건</option>
              <option value="title" class = "selectlist">Title</option>
              <option value="activity" class = "selectlist">Activity</option>
              <option value="price" class = "selectlist">Price</option>
            </select>
          </div>
          <input type="text" class="form-control" placeholder="Search" style="height:50px; border: 1.5px solid #86a5d4 !important;" v-model="word" @keypress.enter="search" />
        </div>

        <div class="hello">
          <div>
            <b-tabs content-class="mt-5" justified active-nav-item-class="font-weight-bold text-uppercase text-primary">
              <b-tab title="All" active @click="settype('all')"></b-tab>
              <b-tab title="Spring" @click="settype('spring')"></b-tab>
              <b-tab title="Fall" @click="settype('autumn')"></b-tab>
              <b-tab title="Winter" @click="settype('winter')"></b-tab>
              <b-tab title="Ground" @click="settype('ground')"></b-tab>
              <b-tab title="Water" @click="settype('water')"></b-tab>
              <b-tab title="Sky" @click="settype('sky')"></b-tab>
            </b-tabs>
          </div>
        </div>

        <!-- <div class="d-flex justify-content-end">
        <a type="button" class="btn btn-outline form-check mb-2" @click="gocreate">
          <i class="fas fa-pen"></i> 상품 등록
        </a>
      </div>-->
      <div class="row justify-content-left" v-if="posts.length > 0">
        <div
          class="col-12 col-sm-6 col-md-3 card-deck"
          style="margin:auto 0; padding:0 20px ;"
          v-for="(post, index) in posts"
          :key="index"
        >
          <div class="card mb-3 profile-post mr-0 ml-0">
            <div class="card-body" style="padding: 0;">
              <div class="box"  @click="getdetail(post.pid)">

              <img :src="makeimgurl(post.imgurl)" v-if="post.imgurl" class="card-img postlist-img" style="height:10rem; box-shadow:5px 5px 5px rgba(0,0,0,.15)" />
              <div
                class="card-img-overlay pt-0 pr-2"
                style="text-align:right; font-size:0.7rem; font-weight:400; color: white; widht:1rem; height:1rem; "
              >
                <!-- <button class="location-button">{{post.location}}</button> -->
                <!-- <p>{{ post.location.substring(0,2) }}</p> -->
                <span class="pr-2 pl-2 pb-1" style="background-color:rgba(0,0,0,0.3);z-index:34;">
                  <i class="fa fa-map-marker" style="font-size:0.7rem;"></i>
                  {{localarea(post.location)}}
                </span>
              </div>
              </div>
              <div class="col-md-12 p-0">
                <div class="card-body" style="padding: 5px; height:10rem;">
                  <!-- tag -->
                  <!-- <div v-for="tagg in tag" :key="tagg.pid">
                  <div v-if="tagg.pid == post.pid" >
                    {{tagg.tag}}
                  </div>
                  </div>-->
                    <!-- </div> -->
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
                    >
                      {{ post.title }}
                    </h5>
                    <div class="card-text mb-2" style="text-align: left; font-size: 0.8rem;">
                      <span style="font-weight:bold;"><i class="fas fa-star mr-1" style="color:Salmon; font-size:0.7rem;"></i>{{ round(post.star) }}</span>
                    </div>
                    <!-- pre-line; -->

                    <div class="text d-flex justify-content-between">
                      <p
                        class="card-text"
                        style="font-size: 1rem; font-weight:bold; text-align: left; text-overflow:ellipsis;overflow: hidden;white-space: nowrap;"
                      >{{ addComma(post.price) }}원</p>
                      <!-- heart like -->
                      <div id="heart" @click="registlike(post.pid)" style="font-weight:bold;">
                        {{ post.likecnt }}
                        <i
                          v-if="check(post.pid)"
                          class="fas fa-heart select-button like-button"
                          style="text-align: right; font-size: 20px; color:crimson;"
                        ></i>
                        <i v-if="!check(post.pid)" class="far fa-heart" style="text-align: right; font-size: 20px;"></i>
                      </div>
                      <!--  -->
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- infinite loading -->
        <infinite-loading :identifier="infiniteId" @infinite="infiniteHandler" spinner="waveDots">
          <div slot="no-results"></div>
        </infinite-loading>
      </div>
    </div>
  </div>
</template>

<script>
import '../../assets/css/postlist.css';
import axios from 'axios';
import InfiniteLoading from 'vue-infinite-loading';
import Swal from 'sweetalert2';

// const Swal = require('sweetalert2')

const baseURL = process.env.VUE_APP_BACKURL;

export default {
  components: {
    InfiniteLoading,
  },
  data() {
    return {
      page: 0,
      infiniteId: 0,
      posts: [],
      key: '',
      word: '',
      type: 'all',
      email: '',
      postLike: [],
      tag: [],
      searchCK: false,
    };
  },
  methods: {
    round(star) {
      return Math.round(star * 10) / 10.0;
    },
    makeimgurl(imgurl) {
      var url = "../../../contents/"+imgurl;
      return url;
    },
    settype(typename) {
      this.key = "";
      this.word = "";
      this.searchCK = false;
      this.type = typename;
      this.infiniteId += 1;
      this.page = 1;
      this.reloading(this.page);
    },

    infiniteHandler($state) {
      if (this.key == '') {
        axios
          .get(`${baseURL}/post/getList/${this.type}/${this.page}`)
          .then((res) => {
            setTimeout(() => {
              if (res.data.length != 0) {
                this.posts = this.posts.concat(res.data);
                $state.loaded();
                this.page += 1;
                if (this.posts.length / 8 < 1) {
                  $state.complete();
                }
                this.nextTag();
              } else {
                $state.complete();
              }
            }, 500);
          })
          .catch((err) => {
            console.log(err);
          });
      } else {
        axios
          .get(`${baseURL}/post/search/${this.type}/${this.key}/${this.word}/${this.page}`)
          .then((res) => {
            setTimeout(() => {
              if (res.data.length != 0) {
                this.posts = this.posts.concat(res.data);
                $state.loaded();
                this.page += 1;
                if (this.posts.length / 8 < 1) {
                  $state.complete();
                }
              } else {
                $state.complete();
              }
            }, 500);
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
    getdetail(pid) {
      scroll(0, 0);
      this.$router.push({
        name: 'PostListDetail',
        params: { ID: pid },
      });
    },
    search() {
      this.page = 1;
      this.infiniteId += 1;

      if (this.key == '') {
        const Toast = Swal.mixin({
          toast: true,
          position: 'top-end',
          showConfirmButton: false,
          timer: 1000,
          timerProgressBar: true,
          onOpen: (toast) => {
            toast.addEventListener('mouseenter', Swal.stopTimer)
            toast.addEventListener('mouseleave', Swal.resumeTimer)
          }
        })

        Toast.fire({
          icon: 'error',
          title: '검색 조건을 선택해주세요!'
        })
        this.word = '';
      } else {
        if (this.word == '') {
          const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 1000,
            timerProgressBar: true,
            onOpen: (toast) => {
              toast.addEventListener('mouseenter', Swal.stopTimer)
              toast.addEventListener('mouseleave', Swal.resumeTimer)
            }
          })

          Toast.fire({
            icon: 'error',
            title: '검색어를 입력해주세요!'
          })
        } else {
          this.searchCK = true;
          this.page = 1;
          axios
            .get(`${baseURL}/post/search/${this.type}/${this.key}/${this.word}/0`)
            .then((res) => {
              this.posts = res.data;
              // this.nextTag();
            })
            .catch((err) => {
              console.log(err);
            });
        }
      }
    },
    registlike(pid) {
      if (this.email != "") {
        axios
          .get(`${baseURL}/like/registDelete/${this.email}/${pid}`)
          .then((res) => {
            this.checklike();
            this.reloading(this.page);
            if (this.check(pid) == false) {
              this.$toasted.show('좋아좋아요', {
                theme: 'bubble',
                position: 'top-right',
                duration: 1000,
              });
            } else {
              this.$toasted.show('좋아요 취소', {
                theme: 'bubble',
                position: 'top-right',
                duration: 1000,
              });
            }
          })
          .catch((err) => {
            alert(err);
          });
      } else {
        Swal.fire({
          icon: 'error',
          text: '로그인 후 이용해주세요...',
          confirmButtonColor: '#fff',
          width: 350,
          confirmButtonText: '<a data-toggle="modal" data-target="#LoginModal" style="font-size:1rem; color:black" >Login</a>',
          showCancelButton: true,
          cancelButtonText: '<a style="font-size:1rem; color:black">Cancel</a>',
          cancelButtonColor: '#fff',
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
      if (this.searchCK) {
        axios
          .get(
            `${baseURL}/post/searchReloading/${this.type}/${this.key}/${
              this.word
            }/${pg - 1}`
          )
          .then((res) => {
            this.posts = res.data;
            // this.nextTag();
          })
          .catch((err) => {
            console.log(err);
          });
      } else {
        axios
          .get(`${baseURL}/post/getThatList/${this.type}/${pg - 1}`)
          .then((res) => {
            this.posts = res.data;
            // this.nextTag();
          })
          .catch((err) => {
            console.log(err);
          });
      }
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
    init() {
      axios
        .get(`${baseURL}/post/getList/${this.type}/0`)
        .then((res) => {
          this.posts = res.data;
          this.nextTag();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    nextTag() {
      this.tag = [];
      for (let i = 0; i < this.posts.length; i++) {
        axios
          .get(`${baseURL}/tag/list/${this.posts[i].pid}`)
          .then((res) => {
            let a = { tag: res.data, pid: this.posts[i].pid };
            this.tag.push(a);
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
    localarea(location) {
      var la = location + '';
      return la.substring(0, 2);
    },
    addComma(num) {
      var regexp = /\B(?=(\d{3})+(?!\d))/g;
      return num.toString().replace(regexp, ',');
    },
  },
  created() {
    this.filter = this.$route.params.TYPE;
    if (this.$cookies.get('Auth-Token') == null) {
      return;
    }
    axios
      .get(`${baseURL}/account/authuser/${this.$cookies.get('Auth-Token')}`)
      .then((response) => {
        this.email = response.data.email;
        this.checklike();
      })
      .catch((err) => {
        console.log(err.response);
      });
  },
  computed: {},
};
</script>

<style>
.nav-item > a {
  height: 50px;
  font-weight: bold;
  padding-top: 15px;
  padding-bottom: 0;
  color: black;
}
</style>