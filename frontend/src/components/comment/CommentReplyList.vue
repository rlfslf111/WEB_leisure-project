<template>
  <div>
    <div style="background-color:rgba(0,0,0,.03);">
      <div class="d-flex justify-content-start">
        <div class="text-muted">
          <i class="fas fa-reply-all rereply ml-3 mt-4 d-flex"></i>
        </div>
        <div class="card" style="width:100%;">
          <div class="card-header pl-2" style="border:none;">
            <!-- nickname 칸 -->
            <div class="d-flex justify-content-between">
              <strong class="text-dark mt-2" style="width:8rem; text-align:left;">
                <b-badge
                  v-if="comType=='normal'"
                  class="mr-2"
                  pill
                  variant
                  style="background-color: #8d9ebd;"
                >일반</b-badge>
                <b-badge
                  v-if="comType=='business'"
                  class="mr-2"
                  pill
                  variant
                  style="background-color: #9bbc88;"
                >판매자</b-badge>
                <b-badge
                  v-if="comType=='admin'"
                  class="mr-2"
                  pill
                  variant
                  style="background-color: #86a5d4;"
                >관리자</b-badge>
                {{reply.nickname}}
              </strong>
              <small
                class="text-dark d-flex align-items-start mt-2"
              >{{commentdate(reply.createDate)}}</small>
            </div>
            <!-- content 칸 -->
            <div class="mt-1 mb-2">
              <CommentReplyUpdate v-if="isUpdate" :reply="reply" />
              <span v-else class="comment d-flex" style="color:#7D7E80; text-align:justify;">{{reply.content}}</span>
            </div>
            <!-- 날짜와 대댓버튼 -->
            <div
              class="ml-auto my-auto datereply d-flex justify-content-end row"
              style="width:40%;"
            >
              <div class="d-flex justify-content-end pl-0 col-12 col-xs-12"></div>
              <!-- 댓글 수정 삭제 버튼 -->
              <div
                class="d-flex justify-content-end pl-0 my-auto col-12"
                style="word-break:nowrap;"
              >
                <small v-if="NickNameCheck | this.checkType == 'admin'" @click="commentReplyModify">
                  <span class="cancelbtn" v-if="isUpdate & NickNameCheck" style="color:Crimson">취소</span>
                  <span
                    class="updatebtn"
                    v-if="!isUpdate & NickNameCheck"
                    style="color:ForestGreen;"
                  >수정</span>
                </small>
                <small
                  @click="replyDelete(reply.rrid)"
                  v-if="NickNameCheck | this.checkType == 'admin'"
                  class="ml-2 deletebtn"
                  style="color:Crimson"
                >삭제</small>
                <!-- <small class="ml-2" style="color:darkKhaki">신고</small> -->
              </div>
            </div>
          </div>
        </div>
      </div>
      <hr class="m-0" />
    </div>
  </div>
</template>

<script>
import axios from "axios";
import CommentReplyUpdate from "./CommentReplyUpdate.vue";

const baseURL = process.env.VUE_APP_BACKURL;

export default {
  components: {
    CommentReplyUpdate,
  },
  props: {
    reply: Object,
  },
  data() {
    return {
      nickName: "",
      NickNameCheck: false,
      isUpdate: false,
      checkType: "",
      comType:"",
    };
  },
  methods: {
    commentdate(createDate) {
      var cd = createDate + "";
      return cd.substring(0, 10) + "  " + cd.substring(11, 16);
    },
    authUser() {
      axios
        .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
        .then((response) => {
          this.checkType = response.data.checkType;
          this.nickName = response.data.nickname;
          if (this.nickName == this.reply.nickname) {
            this.NickNameCheck = true;
          } else {
            this.NickNameCheck = false;
          }
        })
        .catch((err) => {
          console.log(err.response);
        });
    },
    replyDelete(rrid) {
      this.$emit("reply-delete", rrid);
    },
    commentReplyModify() {
      this.isUpdate = !this.isUpdate;
    },
    getType(){
      axios
        .get(`${baseURL}/account/getType/${this.reply.nickname}`)
        .then((response) => {
          this.comType = response.data;
        })
        .catch((err) => {
          console.log(err.response);
        });
    }
  },
  created() {
    if (this.$cookies.get("Auth-Token") != null) this.authUser();
    this.getType();
  },
};
</script>

<style>
.rereply {
  cursor: pointer;
  transform: rotateX(180deg) rotateY(180deg);
  /* transform: rotateX(180deg); */
  /* -webkit-transform:rotateX(180deg);
  -moz-transform:rotateX(180deg); */
}
</style>