export type ActuatorInfo = {
  git: {
    branch: string;
    commit: {
      id: string;
      time: string;
    };
  };
  build: {
    artifact: string;
    name: string;
    time: string;
    version: string;
    group: string;
  };
};
