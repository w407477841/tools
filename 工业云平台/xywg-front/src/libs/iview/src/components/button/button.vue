<template>
  <button :type="htmlType" :class="classes" :disabled="disabled" @click="handleClick">
    <Icon class="ivu-load-loop" type="load-c" v-if="loading"></Icon>
    <Icon :type="icon" v-if="icon && !loading"></Icon>
    <span v-if="showSlot" ref="slot">
      <slot></slot>
    </span>
  </button>
</template>
<script>
import Icon from "../icon";
import { oneOf } from "../../utils/assist";

const prefixCls = "ivu-btn";

export default {
  name: "Button",
  components: { Icon },
  props: {
    type: {
      validator(value) {
        return oneOf(value, [
          "primary",
          "ghost",
          "dashed",
          "text",
          "info",
          "success",
          "warning",
          "error",
          "default"
        ]);
      }
    },
    shape: {
      validator(value) {
        return oneOf(value, ["circle", "circle-outline"]);
      }
    },
    size: {
      validator(value) {
        return oneOf(value, ["small", "large", "default"]);
      }
    },
    loading: Boolean,
    disabled: Boolean,
    plain: Boolean,
    htmlType: {
      default: "button",
      validator(value) {
        return oneOf(value, ["button", "submit", "reset"]);
      }
    },
    icon: String,
    long: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      showSlot: true
    };
  },
  computed: {
    classes() {
      return [
        `${prefixCls}`,
        {
          [`${prefixCls}-${this.type}`]: !!this.type,
          [`${prefixCls}-long`]: this.long,
          [`${prefixCls}-${this.shape}`]: !!this.shape,
          [`${prefixCls}-${this.size}`]: !!this.size,
          [`${prefixCls}-loading`]: this.loading != null && this.loading,
          [`${prefixCls}-icon-only`]:
            !this.showSlot && (!!this.icon || this.loading),
          [`${prefixCls}-plain`]: this.plain
        }
      ];
    }
  },
  methods: {
    handleClick(event) {
      this.$emit("click", event);
    }
  },
  mounted() {
    this.showSlot = this.$slots.default !== undefined;
  }
};
</script>
<style lang="less">
@primary-color          : #2d8cf0;
@info-color             : #2db7f5;
@success-color          : #19be6b;
@warning-color          : #ff9900;
@error-color            : #ed3f14;
@--color-white: #fff;
@--color-black: #000;
@--button-active-shade-percent: 10%;
.btn-plain(@color) {
  color: @color;
  background: mix(@--color-white, @color, 90%);
  border-color: mix(@--color-white, @color, 60%);

  &:hover,
  &:focus {
    background: @color;
    border-color: @color;
    color: @--color-white;
  }

  &:active {
    background: mix(@--color-black, @color, @--button-active-shade-percent);
    border-color: mix(@--color-black, @color, @--button-active-shade-percent);
    color: @--color-white;
    outline: none;
  }

  &.is-disabled {
    &,
    &:hover,
    &:focus,
    &:active {
      color: mix(@--color-white, @color, 40%);
      background-color: mix(@--color-white, @color, 90%);
      border-color: mix(@--color-white, @color, 80%);
    }
  }
}
.ivu-btn-primary.ivu-btn-plain{
    .btn-plain(@primary-color)
}
.ivu-btn-info.ivu-btn-plain{
    .btn-plain(@info-color)
}
.ivu-btn-success.ivu-btn-plain{
    .btn-plain(@success-color)
}
.ivu-btn-warning.ivu-btn-plain{
    .btn-plain(@warning-color)
}
.ivu-btn-error.ivu-btn-plain{
    .btn-plain(@error-color)
}
</style>
