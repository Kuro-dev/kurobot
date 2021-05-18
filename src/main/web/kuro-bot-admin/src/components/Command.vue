<template>

    <div class="command-box">
        <h2>{{ command.command }}</h2>
        <p>{{ command.desc }}</p>
        
        <div>
            <Label v-if="command.functioning" color="green">Functioning</Label>
            <Label v-else color="red">Not functioning</Label>
    
            <Label v-if="command.isListed" color="blue">Listed</Label>
            
            <Label v-if="command.hasReactAction">Reactable</Label>
            
            <Label v-if="command.needsAdmin" color="orange">Admin</Label>
        </div>
        
        <div v-if="command.args.length > 1" :class="{arguments: true, open: showArgs}">
            <p class="arguments-toggle" @click="showArgs = !showArgs">
                <ChevronRightIcon></ChevronRightIcon> Arguments
            </p>
            
            <div v-for="arg of command.args" class="arg">
                <h3>{{ arg.opt }} {{ arg.longOpt }}</h3>
                <p>{{ arg.desc }}</p>
                <p>
                    <b>Parameters count: </b>
                    <span v-if="arg.numberOfArgs === -2">Infinite</span>
                    <span v-if="arg.numberOfArgs === -1">None</span>
                    <span v-if="arg.numberOfArgs > 0">{{arg.numberOfArgs}}</span>
                </p>
            </div>
            
        </div>
        
    </div>
    
</template>

<script>
import Label from "./Label";
import Table from "./Table";
import {ChevronRightIcon} from "vue-feather-icons";

export default {
    name: "Command",
    components: {Table, Label, ChevronRightIcon},
    props: {
        command: Object
    },
    data() {
        return {
            showArgs: false
        }
    }
}
</script>

<style scoped>
.command-box {
    display: flex;
    flex-direction: column;
    align-items: stretch;
    width: 100%;
    border-radius: var(--border-radius);
    margin: 10px;
    padding: 10px 15px;
    box-shadow: var(--box-shadow);
    background-color: white;
}

.command-box > h2 {
    font-size: 24px;
    margin: 0 0 10px 0;
}

.command-box > p {
    margin: 0 0 10px 0;
}

.command-box > div {
    margin-bottom: 10px;
}

.command-box > div:last-child {
    margin-bottom: 0;
}

.command-box .arguments-toggle {
    display: flex;
    align-items: center;
    font-weight: 600;
    cursor: pointer;
    margin: 0 0 10px;
}

.command-box .arguments .feather {
    transition: transform 0.2s ease;
}

.command-box .arguments .arg {
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    margin-bottom: 10px;
}

.command-box .arguments .arg:last-child {
    border-bottom: none;
    margin-bottom: 0;
}

.command-box .arguments .arg h3,
.command-box .arguments .arg p {
    margin: 0 0 10px 0;
}

.command-box .arguments .arg h3 {
    font-size: 18px;
}

.command-box .arguments > .arg {
    display: none;
}

.command-box .arguments.open > .arg {
    display: flex;
    flex-direction: column;
}

.command-box .arguments.open .feather {
    transform: rotate(90deg);
}
</style>