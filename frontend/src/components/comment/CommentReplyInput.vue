<template>
  <!-- <div class="form-row align-items-center d-flex justify-content-end">
  <div class="col-auto" style="width:100%">-->
  <div class="d-flex justify-content-between m-0 mt-2 mb-2 p-0">
    <!-- <div class="input-group-prepend my-auto">
          <strong class="text-dark my-auto" style="height: 2.5rem; border: none;">{{nickname}}</strong>
    </div>-->
    <textarea
      v-model="commentReplyData.content"
      style="height:3.5rem; border-radius : 0px; width:90%;!important"
      type="text"
      class="form-control my-auto p-1"
      id="inlineFormInputGroup"
      placeholder="댓글을 남겨주세요."
    ></textarea>
    <!-- button -->
    <div class="buttonComment d-flex align-items-center" style="height:3.5rem; width:10%;">
      <span class="bg-white mx-auto" @click="CommentReplyCreate">
        <i class="far fa-2x fa-keyboard" style="font-size:25px"></i>
      </span>
    </div>
  </div>
  <!-- </div>
  </div>-->
</template>

<script>
import axios from "axios";
const baseURL = process.env.VUE_APP_BACKURL;
export default {
  data() {
    return {
      commentReplyData: {
        rid: "",
        nickname: "",
        content: "",
      },
      nickname: "",
    };
  },
  props: {
    comment: Object,
  },
  methods: {
    authUser() {
      axios
        .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
        .then((response) => {
          this.commentReplyData.nickname = response.data.nickname;
          this.nickname = response.data.nickname;
        })
        .catch((err) => {
          console.log(err.response);
        });
    },
    CommentReplyCreate() {
      if (this.commentReplyData.content == "") {
        Swal.fire({
          width: 250,
          icon: "warning",
          text: "답글을 남겨주세요!",
          confirmButtonColor: "#fff",
          confirmButtonText:
            '<a style="font-size:1rem; color:black; width:0.5rem">확인</a>',
        });
      } else {
        this.$emit("creply-create", this.commentReplyData);
      }
    },
  },
  created() {
    this.commentReplyData.rid = this.comment.rid;
    this.authUser();
  },
};
</script>

<style>
</style>