<template>
  <div class="container col-sm-12 col-md-12 col-lg-12 p-0">
    <!-- background image -->
    <div class="basket-img" style="display:block;">
      <div class="basket-img-bg"></div>
      <div class="postment d-flex ml-5">장바구니</div>
      <p class="postmentp d-flex ml-5">액티비티를 패키징하여 저렴한 가격으로!</p>
    </div>

    <div class="container col-md-10">
      <!-- shooping list  -->
      <div class="d-flex justify-content-between mt-5 mb-3">
        <p class="shopping-list" style="font-weight:bold;">
          <i class="fas fa-shopping-basket mr-2"></i>Shopping List
        </p>
        <button class="btn btn-delete" @click="checkdelete" style="font-weight:bold;">
          <i class="fas fa-trash-alt mr-2"></i>
          <span style="font-size:1rem;">선택항목 삭제하기</span>
        </button>
      </div>
      <div v-if="carts.length > 0">
        <div class="input-group mb-5" v-for="(post, index) in carts" :key="index">
          <div class="input-group-prepend">
            <div class="input-group-text">
              <input
                style="width:1.1rem; height:1.1rem;"
                type="checkbox"
                aria-label="Checkbox for following text input"
                :value="post"
                v-model="checked"
                @click="changetf((page - 1) * 8 + index)"
              />
            </div>
          </div>
          <img :src="makeimgurl(post.imgurl)" v-if="post.imgurl" alt @click="getdetail(post.pid)" />
          <div
            type="text"
            class="basket-list col-md-8"
            aria-label="Text input with checkbox"
            @click="getdetail(post.pid)"
          >
            <p class="mb-0">제목 : {{ post.title }}</p>
            <p class="mb-0">기간 : {{ post.sdate }}~{{ post.edate }}</p>
            <p class="mb-0">위치 : {{ post.location }}</p>
            <p class="mb-0">가격 : {{ post.price }}</p>
            <!-- <p>{{checked}}</p> -->
          </div>
        </div>

        <!-- price -->
        <div>
          <p class="checked-price">Total : {{ checkedprice }} 원</p>
        </div>

        <!-- 구매하기 button -->
        <div class="d-flex justify-content-end mb-5">
          <button
            class="btn btn-default"
            style="background-color:#86a5d4;color:white;font-weight:bold;"
            data-toggle="modal"
            data-target="#BasketPackingModal"
            @click="btnClick"
          >
            <i class="far fa-hand-point-up mr-2"></i>패키징
          </button>
          <BasketPackingModal v-if="!istemp" :prePosts="prePosts" />
        </div>
      </div>

      <!-- paging -->
      <b-pagination v-model="page" :total-rows="len" pills :per-page="8" style="align:center;"></b-pagination>

      <!-- 장바구니가 비어있을 때 -->
      <div class="col" v-if="carts.length <= 0">
        <div class="mt-5 mb-3" style="font-weight:bold;">장바구니가 비어있습니다.</div>
        <button
          type="button"
          class="btn mb-5"
          @click="goPost()"
          style="background-color: rgb(134, 165, 212); color: white; font-weight:bold;"
        >상품보러 가기</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import "../../assets/css/basket.css";
import BasketPackingModal from "../../components/modal/BasketPackingModal.vue";
import BPagenation from "bootstrap-vue";

import Swal from "sweetalert2";

const baseURL = process.env.VUE_APP_BACKURL;

export default {
  components: {
    BPagenation,
    BasketPackingModal,
  },
  created() {
    this.authUser();
  },
  data() {
    return {
      page: 1,
      carts: {
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
      checked: [],
      sum: 0,
      likes: {
        no: "",
        pid: "",
        email: "",
        cart: "",
      },
      len: 0,
      no: {},
      temp: [],
      clicknum: 0,
      prePosts: [],
      istemp : false,
    };
  },
  methods: {
    makeimgurl(imgurl) {
      var url = "../../../contents/" + imgurl;
      return url;
    },
    btnClick() {
      this.temp = [];
      for (let i = 0; i < this.len; i++) {
        if (this.no[i] == 1) {
          this.temp.push(this.likes[i].no);
        }
      }
      if (this.temp == "") {
        this.istemp=true;
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
          title: '선택 항목이 없습니다.'
        })
        return;
      } else {
        this.istemp=false;
        axios
          .get(`${baseURL}/cart/preview/${this.temp}`)
          .then((res) => {
            this.prePosts = res.data;
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
    changetf(index) {
      if (this.no[index] == null) {
        this.no[index] = 1;
      } else {
        this.no[index] = null;
      }
    },
    authUser() {
      axios
        .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
        .then((response) => {
          this.email = response.data.email;
          this.init();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    init() {
      this.likeCheck();
    },
    likeCheck() {
      axios
        .get(`${baseURL}/cart/likelist/${this.email}`)
        .then((res) => {
          this.likes = res.data;
          this.len = this.likes.length;
          this.checkPage();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getdetail(pid) {
      scroll(0, 0);
      this.$router.push({
        name: "PostListDetail",
        params: { ID: pid },
      });
    },
    checkPage() {
      axios
        .get(`${baseURL}/cart/list/${this.email}/${this.page}`)
        .then((res) => {
          this.carts = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
      scroll(0, 0);
    },
    checkdelete() {
      this.btnClick();

      Swal.fire({
        width: 350,
        text: "선택항목을 삭제하시겠습니까?",
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
            title: "장바구니에서 삭제되었습니다.",
          });
          axios
            .delete(`${baseURL}/cart/delete/${this.temp}`)
            .then(() => {
              // this.$router.push(`/user/basket`);
              this.checked = [];
              this.init();
            })
            .catch((error) => {
              console.log(error);
            });
        }
      });
    },
    goPost() {
      this.$router.push("/posts");
    },
  },
  computed: {
    checkedprice(price) {
      this.sum = 0;
      for (var i = 0; i < this.checked.length; i++) {
        this.sum += this.checked[i].price;
      }
      return this.sum;
    },
  },
  watch: {
    page: function (v) {
      this.checkPage();
    },
  },
};
</script>

<style>
.pagination {
  display: flex;
  justify-content: center;
  margin-bottom: 2rem;
}
</style>
