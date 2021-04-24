<template>
  <div>
    <!-- 대댓글 Input -->
    <CommentReplyInput v-if="this.checkType == 'business' | this.nickname == comment.nickname | this.checkType == 'admin'" :comment="comment" @creply-create="CommentReplyCreate"/>
    <!-- 대댓글 List -->
    <div v-if="flag">
      <CommentReplyList v-for="reply in receiveReply" :key="reply.rrid" :reply="reply" @reply-delete="replyDelete"/>
    </div>
    <div v-if="!flag">
      <small><i class="far fa-surprise mr-1 mb-3"></i>등록된 답변이 없습니다.<i class="far fa-surprise ml-1"></i></small>
    </div>
  </div>
</template>

<script>
import CommentReplyInput from './CommentReplyInput.vue'
import CommentReplyList from './CommentReplyList.vue'

import axios from 'axios'
const baseURL = process.env.VUE_APP_BACKURL;

export default {
    data: function() {
      return {
        nickname: "",
        commentrid: "",
        checkType: "",
        receiveReply: [],
        flag: true,
      }
    },
    props: {
        comment: Object,
    },
    components: {
      CommentReplyInput,
      CommentReplyList,
    },
    methods: {
      authUser() {
        axios
          .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
          .then((response) => {
            this.fetchCommentReply()
            this.nickname = response.data.nickname
            this.checkType = response.data.checkType
          })
          .catch((err) => {
            console.log(err.response);
          });
      },
      CommentReplyCreate(commentReplyData) {
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
        axios.post(`${baseURL}/reply/reRegist`,commentReplyData)
          .then(() => {
            commentReplyData.content = ''
            this.fetchCommentReply()
            Toast.fire({
              icon: "success",
              title: "답글 작성 완료!",
            });
          }).catch((err) => {
            console.log(err)
          })
      },
      fetchCommentReply() {
        axios.get(`${baseURL}/reply/reList/${this.commentrid}`)
          .then((response) => {
            this.receiveReply = response.data
            if (this.receiveReply.length > 0) {
              this.flag = true
            } else {
              this.flag = false
            }
          }).catch((error) => {
            console.log(error)
          })
      },
      replyDelete(rrid) {
        Swal.fire({
          width: 350,
          text: "답글을 삭제하시겠습니까?",
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
              title: "답글이 삭제되었습니다.",
            });
            axios
              .delete(`${baseURL}/reply/reDelete/${rrid}`)
              .then((response) => {
                this.fetchCommentReply();
              })
              .catch((error) => {
                console.log(error.response.data);
              });
          }
        });
      },
    },
    created() {
      this.commentrid = this.comment.rid
      if(this.$cookies.get('Auth-Token')!=null){
      this.authUser()
      }else{
        this.fetchCommentReply()
      }
    },
}
</script>

<style>

</style>