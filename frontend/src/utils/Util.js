export function isEmptyOrSpaces(str){
    return str === null || str.match(/^\s*$/) !== null;
}