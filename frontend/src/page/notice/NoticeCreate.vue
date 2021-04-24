<template>
  <div class="container col-sm-12 col-md-12 col-lg-12 p-0">
    <div class="notice-img" style="display:block;"></div>
    <div class="container col-md-8">
      <!-- Title -->
      <div class="form-group form-group mb-0 mt-5 d-flex justify-content-between">
        <div class="d-flex justify-content-start">
          <label class="d-flex notice-title-font mr-3">Title</label>
          <small
            class="form-text notice-content-font text-muted d-flex"
            v-if="!error.title"
          >제목을 입력하세요.</small>
        </div>
      {{NoticeCreate.content}}
        <div>
          <select
            class="form-control col-md-12"
            id="imp"
            v-model="NoticeCreate.importance"
            style="float:right;"
          >
            <option value="0">중요도</option>
            <option value="1">일반</option>
            <option value="2">중요</option>
            <option value="3">필독</option>
          </select>
        </div>
      </div>
      <div class="mb-4">
        <input type="text" class="form-control" id="title" v-model="NoticeCreate.title" />
        <!-- <small class="form-text notice-content-font text-muted d-flex" v-if="!error.title">제목을 입력하세요.</small> -->
        <small class="form-text d-flex" style="color:red;" v-if="error.title">{{ error.title }}</small>
      </div>

      <!-- Detail-Info -->
      <div class="form-group">
        <div class="d-flex justify-content-start">
          <label class="d-flex notice-title-font mr-3">Detail-Info</label>
          <small
            class="form-text notice-content-font text-muted d-flex"
            v-if="!error.content"
          >내용을 입력하세요.</small>
        </div>

      
           <Editor ref="toastuiEditor" />
         
      
        <small class="form-text d-flex" style="color:red;" v-if="error.detail">{{ error.detail }}</small>
      </div>
      <!-- Button -->
      <div class="d-flex justify-content-end mb-5">
        <button
          type="submit"
          class="btn btn-outline pr-0"
          style="font-size: 1.1rem; color:red; font-weight:bold;"
          @click="noticeregist()"
        >
          <i class="fas fa-pen mr-1"></i>등록
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import "../../assets/css/noticecreate.css";

import 'codemirror/lib/codemirror.css'; 
import '@toast-ui/editor/dist/toastui-editor.css';
import { Editor } from '@toast-ui/vue-editor';


import Swal from "sweetalert2";
// import { component } from 'vue/types/umd';

const baseURL = process.env.VUE_APP_BACKURL;

export default {
  components:{
    Editor
  },
  data() {
    return {
      page: 1,
      NoticeCreate: {
        title: "",
        content: "",
        importance: "0"
      },
      
      email: "",
      superadmin: "admin@linkivity.com",
      error: {
        title: false,
        content: false,
      },
    };
  },
  created() {
    this.chcekadmin();
  },
  // watch: {
  //   newContent: function(v) {
  //     this.newContent = this.NoticeCreate.content;
  //   }
  // },
  methods: {
    chcekadmin() {
      axios
        .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
        .then((response) => {
          this.email = response.data.email;
          if (this.email != this.superadmin) {
            let timerInterval;
            Swal.fire({
              title: "관리자만 글 작성가능합니다!",
              html: "<b></b> 후 전 페이지로 돌아갑니다.",
              timer: 1000,
              timerProgressBar: true,
              onBeforeOpen: () => {
                Swal.showLoading();
                timerInterval = setInterval(() => {
                  const content = Swal.getContent();
                  if (content) {
                    const b = content.querySelector("b");
                    if (b) {
                      b.textContent = Swal.getTimerLeft();
                    }
                  }
                }, 100);
              },
              onClose: () => {
                clearInterval(timerInterval);
                this.$router.go(-1);
              },
            }).then((result) => {
              /* Read more about handling dismissals below */
              if (result.dismiss === Swal.DismissReason.timer) {
                console.log("I was closed by the timer");
              }
            });
            // Swal.fire({
            //   title: "관리자만 작성가능합니다.",
            //   showClass: {
            //     popup: "animate__animated animate__fadeInDown",
            //   },
            //   hideClass: {
            //     popup: "animate__animated animate__fadeOutUp",
            //   },
            // });
            // setTimeout(() => {
            //   this.$router.go(-1);
            // }, 1000);
          }
        })
        .catch((err) => {
          console.log(err.response);
        });
    },
    noticeregist(notice) {
      var content = this.$refs.toastuiEditor.invoke("getMarkdown");
      this.NoticeCreate.content = content
      var flag = 0;
      if (this.NoticeCreate.title == "") {
        this.error.title = "제목을 입력해주세요.";
        flag = 1;
      } else {
        this.error.title = false;
      }
      if (this.NoticeCreate.content == "") {
        this.error.content = "내용을 입력해주세요.";
        flag = 1;
      } else {
        this.error.content = false;
      }
      if (flag == 1) {
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
          title: '정보를 모두 입력해주세요!'
        })
        return;
      }
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

      axios
        .post(`${baseURL}/notice/regist/`, this.NoticeCreate)
        .then((response) => {
          Toast.fire({
            icon: "success",
            title: "작성이 완료되었습니다.",
          });
          this.$router.push(`/notice`);
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
</script>

<style scoped>
</style>