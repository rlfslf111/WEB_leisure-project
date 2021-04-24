<template>
  <div>
    <div class="card">
      <div class="card-header bg-white">
        <!-- nickname 칸 -->

        <div class="d-flex justify-content-between">
          <strong class="text-dark mt-2">
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
            {{comment.nickname}}
          </strong>
          <small class="text-dark d-flex align-items-start mt-2">{{commentdate(comment.createDate)}}</small>
        </div>
        <!-- content 칸 -->
        <div class="mt-2 mb-2">
          <!-- 댓글 수정 -->
          <CommentUpdate v-if="isUpdated" :comment="comment" />
          <span v-else class="comment d-flex" style="color:#7D7E80; text-align:justify;">{{comment.content}}</span>
        </div>
        <div class="d-flex justify-content-between mb-2" style="cursor:pointer;">
          <small @click="replyInputCheck" class="d-flex text-muted" style="font-weight:bold;">댓글보기</small>
          <!-- 댓글 수정 삭제 버튼 -->
          <div class="d-flex justify-content-end my-auto" style="word-break:nowrap;">
            <small v-if="NickNameCheck | this.checkType == 'admin'" @click="commentModify">
              <span class="cancelbtn" v-if="isUpdated & NickNameCheck" style="color:Crimson">취소</span>
              <span class="updatebtn" v-if="!isUpdated & NickNameCheck" style="color:ForestGreen">수정</span>
            </small>
            <small
              v-if="NickNameCheck | this.checkType == 'admin'"
              class="ml-2 deletebtn"
              style="color:Crimson"
              @click="commentDelete"
            >삭제</small>
            <!-- <small class="ml-2" data-toggle="modal" data-target="#commentIndict" style="color:DarkKhaki">신고</small> -->
          </div>
        </div>
        <CommentReplyAnswer v-if="replyCheck" :comment="comment" />
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import CommentUpdate from "./CommentUpdate";
import CommentReplyAnswer from "./CommentReplyAnswer.vue";

const baseURL = process.env.VUE_APP_BACKURL;

export default {
  components: {
    CommentUpdate,
    CommentReplyAnswer,
  },
  props: {
    comment: Object,
  },
  data: function () {
    return {
      NickName: "",
      NickNameCheck: false,
      isUpdated: false,
      email: "",
      rid: "",
      replyCheck: false,
      checkType: "",
      comType:"",
      currentNickname: "",
    };
  },
  methods: {
    authUser() {
      axios
        .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
        .then((response) => {
          this.email = response.data.email;
          this.checkType = response.data.checkType;
          this.currentNickname = response.data.nickname;
          this.fetchNickName();
        })
        .catch((err) => {
          console.log(err.response);
        });
    },
    commentDelete: function () {
      this.$emit("comment-delete", this.comment);
    },
    commentModify() {
      this.isUpdated = !this.isUpdated;
    },
    fetchNickName() {
      axios
        .get(`${baseURL}/account/getNickname/${this.email}`)
        .then((response) => {
          this.NickName = response.data;
          if (this.NickName == this.comment.nickname) {
            this.NickNameCheck = true;
          } else {
            this.NickNameCheck = false;
          }
        })
        .catch((err) => {
          console.log(err.response.data);
        });
    },
    fetchCommentRID() {
      this.rid = this.comment.rid;
    },
    commentdate(createDate) {
      var cd = createDate + "";
      return cd.substring(0, 10) + "  " + cd.substring(11, 16);
    },
    replyInputCheck() {
      this.replyCheck = !this.replyCheck;
    },
    getType(){
      axios
        .get(`${baseURL}/account/getType/${this.comment.nickname}`)
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
.comment {
  word-break: break-all;
}
.cancelbtn {
  cursor: pointer;
}
.updatebtn {
  cursor: pointer;
}
.deletebtn {
  cursor: pointer;
}
</style>