<template>
  <div>
    <!-- cart list가 없을때 -->
    <div class="mt-5" v-if="cartPosts.length <= 0">
      <i class="fas fa-surprise mt-5 mr-2"></i
      ><span style="font-weight:bold">장바구니가 비어있습니다.</span
      ><i class="fas fa-surprise ml-2"></i>
      <br />
      <button
        @click="goPost"
        class="btn mt-2"
        style="font-weight:bold; color:white; background-color:RGB(134, 165, 212); border-radius:7px"
      >
        <i class="fas fa-shopping-basket mr-2"></i>장바구니 채우러 가기
      </button>
    </div>
    <!-- cart list가 있을때 -->
    <div class="row justify-content-left" v-if="cartPosts.length > 0">
      <div
        class="col-6 col-sm-6 col-md-4 col-lg-3 card-deck"
        style="margin:auto 0;"
        v-for="(cartPost, cindex) in cartPosts"
        :key="cindex"
      >
        <div class="card mb-3 profile-post mr-0 ml-0">
          <div class="card-body" style="padding: 0 30px;">
            <img
              :src="makeimgurl(cartPost.imgurl)"
              v-if="cartPost.imgurl"
              class="card-img"
              @click="getdetail(cartPost.pid)"
              style="height:10rem; box-shadow:5px 5px 5px rgba(0,0,0,.15)"
            />
            <!-- <div
              class="card-img-overlay"
              style="padding:4rem 0; text-align:center; font-size:1.3rem; font-weight:bold; color: white;"
            ></div> -->
            <div class="col-md-12 p-0">
              <div class="card-body" style="padding: 5px;">
                <!-- <p
                  class="card-text mb-2"
                  style="font-size: 1rem; text-align: left; text-overflow:ellipsis;overflow: hidden;white-space: nowrap; color:gray"
                >
                  {{ cartPost.sdate }}~{{ cartPost.edate }}
                </p> -->
                <h5
                  class="card-title m-0"
                  @click="getdetail(cartPost.pid)"
                  style="font-weight:bold;font-size: 1rem; text-align: left; cursor:pointer; text-overflow:ellipsis;overflow: hidden;white-space: nowrap;"
                >
                  {{ cartPost.title }}
                </h5>
                <div
                  class="card-text mb-3"
                  style="text-align: left; font-size: 0.8rem;"
                >
                  <span style="font-weight:bold;">
                    <i
                      class="fas fa-star mr-1"
                      style="color:Salmon; font-size:0.7rem;"
                    ></i>
                    {{ round(cartPost.star) }}
                  </span>
                </div>
                <div class="text d-flex justify-content-between">
                  <p
                    class="card-text"
                    style="font-weight:bold;font-size: 1rem; text-align: left; text-overflow:ellipsis;overflow: hidden;white-space: nowrap;"
                  >
                    {{ cartPost.price }}원
                  </p>
                  <button
                    type="button"
                    class="btn btn-outline-danger btn-sm"
                    style="font-weight:bold;height:30px;"
                    @click="deleteCart(cartPost.pid)"
                  >
                    삭제
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- paging -->
    <b-pagination
      v-if="ctotalPage > 8"
      v-model="cpage"
      :total-rows="ctotalPage"
      pills
      :per-page="8"
    ></b-pagination>
    <br />
    <br />
    <br />
  </div>
</template>

<script>
// import '../../assets/css/postlist.css';
import axios from "axios";
import BPagenation from "bootstrap-vue";
import Swal from "sweetalert2";

const baseURL = process.env.VUE_APP_BACKURL;

export default {
  components: {
    BPagenation,
  },
  data() {
    return {
      cartPosts: {
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
      cpage: 1,
      ctotalPage: 0,
    };
  },
  methods: {
    authUser() {
      axios
        .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
        .then((res) => {
          this.email = res.data.email;
          this.init();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    init() {
      axios
        .get(`${baseURL}/cart/list/${this.email}/${this.cpage}`)
        .then((res) => {
          this.cartPosts = res.data;
        })
        .catch((err) => {
          console.log(err);
        });

      axios
        .get(`${baseURL}/cart/count/${this.email}`)
        .then((res) => {
          this.ctotalPage = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    makeimgurl(imgurl) {
      var url = "../../../contents/"+imgurl;
      return url;
    },
    getdetail(pid) {
      this.$router.push({
        name: "PostListDetail",
        params: { ID: pid },
      });
    },
    pageClick(pageNum) {
      this.page = pageNum - 1;
    },
    checkPage() {
      axios
        .get(`${baseURL}/cart/list/${this.email}/${this.cpage}`)
        .then((res) => {
          this.cartPosts = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    deleteCart(pid) {
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
            title: "삭제되었습니다.",
          });
          axios
            .delete(`${baseURL}/cart/deleteCart/${pid}/${this.email}/1`)
            .then((res) => {
              this.init();
            })
            .catch((err) => {
              console.log(err);
            });
        }
      });
    },
    round(star) {
      return Math.round(star * 10) / 10.0;
    },
    goPost() {
      this.$router.push("/posts");
    },
  },
  watch: {
    cpage: function(v) {
      this.checkPage();
    },
  },
  created() {
    this.authUser();
  },
};
</script>

<style scoped>
.card-img:hover {
  transform: scale(1.05);
  transition: all 0.3s ease-in-out;
  cursor: pointer;
}
</style>
