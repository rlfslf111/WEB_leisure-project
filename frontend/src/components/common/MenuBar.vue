<template>
  <div v-if="this.scrollposition > 200" class="side-menu">
    <i class="fas fa-2x fa-angle-double-up upBtn" @click="toTop" style="cursor:pointer;"></i>
    <b-button v-b-toggle.sidebar-2 class="side-main-button btn btn-light" style="background-color:rgba(255,255,255,0); border:none;">
      <i class="fas fa-2x fa-bars" style="color:rgb(134, 165, 212)"></i>
      <b-sidebar id="sidebar-2" no-header-close title="Menu" shadow width="250px">
          <div class="ml-4" style="text-align:left">
            <ul class="navbar-nav nav-sub ml-auto">
              <li class="nav-item">
                <a class="nav-link mt-3 hamburger2" @click="goPost">
                  <i class="fas fa-stream mr-2"></i>
                  액티비티
                </a>
              </li>
              <li class="nav-item" v-if="this.$cookies.isKey('Auth-Token') && usertype == 'business'">
                <a class="nav-link mt-3  hamburger2" @click="gocreate">
                  <i class="fas fa-pen mr-2"></i>
                  액티비티 등록
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link mt-3  hamburger2" @click="goNotice">
                  <i class="fas fa-flag mr-2"></i>
                  공지사항
                </a>
              </li>
              
              <li class="nav-item" v-if="this.$cookies.isKey('Auth-Token') && usertype == 'normal'">
                <a class="nav-link mt-3 hamburger2" @click="goBasket">
                  <i class="fas fa-shopping-basket mr-2"></i>
                  장바구니
                </a>
              </li>

              <li class="nav-item" v-if="this.$cookies.isKey('Auth-Token') && usertype != 'admin'">
                <a @click="info" class="nav-link mt-3 hamburger2">
                  <i class="far fa-user mr-2"></i>
                  마이페이지
                </a>
              </li>
              <li class="nav-item" v-if="this.$cookies.isKey('Auth-Token') && usertype == 'admin'">
                <a @click="goadmin" class="nav-link mt-3 hamburger2">
                  <i class="fas fa-users-cog mr-2"></i>
                  관리 페이지
                </a>
              </li>
              <li class="nav-item">
                <a v-if="this.$cookies.isKey('Auth-Token')" @click="logout" class="nav-link mt-3  hamburger">
                  <i class="fas fa-sign-out-alt mr-2"></i>
                  로그아웃
                </a>
              </li>
              <li class="nav-item">
                <a v-if="!this.$cookies.isKey('Auth-Token')" data-toggle="modal" data-target="#LoginModal" class="nav-link mt-3  hamburger">
                  <i class="fas fa-sign-in-alt mr-2"></i>
                  로그인
                </a>
              </li>
            </ul>
          </div>
        </b-sidebar>
    </b-button>
    <!-- <ul v-if="this.showbar == true" class="side-menu-bar" style="float:bottom;">
            <li @click="goPost"><i class="fas fa-stream mx-2 "></i>Post</li>
            <li @click="goNotice"><i class="fas fa-flag mx-2"></i>Notice</li>
            <li v-if="this.$cookies.isKey('Auth-Token') && this.usertype == 'business'" @click="gocreate"><i class="fas fa-pen mx-2"></i>Write</li>
            <li v-if="this.$cookies.isKey('Auth-Token') && this.usertype == 'normal'" @click="goBasket"><i class="fas fa-shopping-basket mx-2"></i>Basket</li>
            <li v-if="this.$cookies.isKey('Auth-Token') && this.usertype != 'admin'" @click="info"><i class="far fa-user mx-2"></i>Mypage</li>
            <li v-if="this.$cookies.isKey('Auth-Token') && this.usertype == 'admin'" @click="info"><i class="fas fa-users-cog mr-2"></i>Admin</li>
            <li @click="toTop" style="margin-bottom:3.5rem;"><i class="fas fa-angle-double-up upBtn" style="cursor:pointer;"></i>Top</li>
          </ul>
        <button class="side-main-button" @click="changeshowbar"><i class="fas fa-bars"></i></button> -->

  </div>
</template>

<script>
import axios from 'axios';
import '../../assets/css/menubar.css';

const baseURL = process.env.VUE_APP_BACKURL;

export default {
  props: {
    scrollposition: Number,
    showbar: Boolean,
  },
  data() {
    return {
      usertype: '',
    };
  },
  created() {
    if (this.$cookies.get('Auth-Token') != null) {
      this.authUser();
    }
  },
  methods: {
    authUser() {
      axios
        .get(`${baseURL}/account/authuser/${this.$cookies.get('Auth-Token')}`)
        .then((response) => {
          this.usertype = response.data.checkType;
        })
        .catch((err) => {
          console.log(err.response);
        });
    },
    goadmin() {
      scroll(0, 0);
      this.$router.push('/admin').catch((err) => {
        this.$router.go();
      });
    },
    gocreate() {
      scroll(0, 0);
      this.$router.push({
        name: 'PostCreate',
      }).catch(err => {
        this.$router.go();
      });
    },
    logout: function() {
      this.$cookies.remove('Auth-Token');
      scroll(0, 0);
      this.$router.push('/').catch((err) => {
        this.$router.go();
      });
      Swal.fire({
        width: 250,
        position: 'top-right',
        icon: 'success',
        title: '로그아웃 완료!!!',
        showConfirmButton: false,
      });
      setTimeout(() => {
        this.$router.go();
      }, 1000);
    },
    info: function() {
      scroll(0, 0);
      this.$router.push('/user/info/').catch(err => {
        this.$router.go();
      });
    },
    goPost: function() {
      scroll(0, 0);
      this.$router.push('/posts/').catch(err => {
        this.$router.go();
      });
    },
    goBasket: function() {
      this.$router.push('/user/basket/').catch(err => {
        this.$router.go();
        });
    },
    goNotice: function() {
      this.$router.push('/notice/').catch(err => {
        this.$router.go();
      });
    },
    // changeshowbar() {
    //   this.$emit('change-showbar', this.showbar);
    // },
    toTop() {
      scroll(0, 0);
      // this.changeshowbar();
    },
  },
};
</script>

<style scoped>
.nav-link {
    font-size: 0.9rem;
    cursor: pointer;
    font-weight: bold;
}
.hamburger2 {
  color: black !important;
}
</style>