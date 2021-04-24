<template>
  <div id="app">
    <Header :isHeader="isHeader" />
    <LoginModal />
    <FindPasswordModal />
    <router-view />
    <Menubar :scrollposition="scrollposition" @change-showbar="changeshowbar" :showbar="showbar" />
    <Footer />
  </div>
</template>

<script src="https://use.fontawesome.com/releases/v5.2.0/js/all.js"></script>
<script>
import Header from './components/common/Header.vue';
import constants from './lib/constants';
import LoginModal from './components/modal/LoginModal.vue';
import Footer from './components/common/Footer.vue';
import Menubar from './components/common/MenuBar.vue';
import FindPasswordModal from './components/modal/FindPasswordModal';

export default {
  name: 'App',
  components: {
    Header,
    LoginModal,
    Footer,
    Menubar,
    FindPasswordModal,
  },
  created() {
    let url = this.$route.name;
    this.checkUrl(url);
    window.addEventListener('scroll',this.updatescroll);
  },
  watch: {
    $route(to) {
      this.checkUrl(to.name);
    },
  },
  methods: {
    checkUrl(url) {
      let array = [constants.URL_TYPE.USER.LOGIN];

      let isHeader = true;
      array.map((path) => {
        if (url === path) isHeader = false;
      });
      this.isHeader = isHeader;
    },
    updatescroll(){
            this.scrollposition = window.scrollY
            // 퍼센트계산
            // this.scrollposition = Math.floor(window.scrollY) / ($(document).height())
            // console.log(this.scrollposition)
            },
    changeshowbar(showbar){
            if(this.showbar == true){
                this.showbar = false
            }else{
                this.showbar = true
            }
            // console.log(this.showbar)
            },
  },
  data() {
    return {
      isHeader: true,
      constants,
      scrollposition:0,
      showbar:false,
    };
  },
};
</script>

<style>
#app {
  /* font-family: 'IBMPlexSansKR-Text'; */
  /* font-family: 'CookieRunOTF-Bold'; */
  font-family: 'Nanum Gothic', sans-serif;
  /* font-family: 'Jua', sans-serif; */
  /* font-family: 'Lexend Zetta', sans-serif; */
  /* font-family: Avenir, Helvetica, Arial, sans-serif; */
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  /* margin-top: 7rem; */
}

/* @font-face {
    font-family: 'IBMPlexSansKR-Text';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/IBMPlexSansKR-Text.woff') format('woff');
    font-weight: normal;
    font-style: normal;
} */

@font-face {
    font-family: 'CookieRunOTF-Bold';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_twelve@1.0/CookieRunOTF-Bold00.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}
            
            
html {
  scroll-behavior: smooth;
}
</style>
