debugger
export default (name) => () => import(`@/components/page${name}.vue`)