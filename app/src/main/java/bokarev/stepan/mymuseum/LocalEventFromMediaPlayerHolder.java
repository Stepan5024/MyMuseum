/*
 * Copyright 2018 Nazmul Idris. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bokarev.stepan.mymuseum;


class LocalEventFromMediaPlayerHolder {

    static class UpdateLog {

        final StringBuffer formattedMessage;

        UpdateLog(StringBuffer formattedMessage) {

            this.formattedMessage = formattedMessage;
        }
    }

    static class PlaybackDuration {

        final int duration;

        PlaybackDuration(int duration) {

            this.duration = duration;
        }
    }

    static class PlaybackPosition {

        final int position;

        PlaybackPosition(int position) {
            this.position = position;
        }
    }

    static class PlaybackCompleted {

    }

    static class StateChanged {

        final MediaPlayerHolder.PlayerState currentState;

        StateChanged(MediaPlayerHolder.PlayerState currentState) {
            this.currentState = currentState;
        }
    }

}
